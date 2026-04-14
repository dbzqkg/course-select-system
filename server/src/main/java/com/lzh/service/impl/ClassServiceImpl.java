package com.lzh.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.common.constants.RedisConstants;
import com.lzh.common.result.PageResult;
import com.lzh.common.util.BitMapUtils;
import com.lzh.common.util.ThreadLocalUtil;
import com.lzh.mapper.CourseClassMapper;
import com.lzh.pojo.dto.ClassDTO;
import com.lzh.pojo.vo.ClassVO;
import com.lzh.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.lzh.common.constants.RedisConstants.CLASS_SCHEDULE_BIT_MAP;

@Service
@Slf4j
public class ClassServiceImpl implements ClassService {

    @Autowired
    private CourseClassMapper courseClassMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询：ZSet 索引分页 + Pipeline 批量获取 Hash 字段
     */
    @Override
    public PageResult page(ClassDTO dto) {
        if (StringUtils.hasText(dto.getName())) {
            return queryFromDb(dto);
        }

        long start = (dto.getPage() - 1) * dto.getSize();
        long end = start + dto.getSize() - 1;

        Set<String> classIds = stringRedisTemplate.opsForZSet().range(RedisConstants.CLASS_IDX_ALL, start, end);
        if (classIds == null || classIds.isEmpty()) return queryFromDb(dto);
        Long total = stringRedisTemplate.opsForZSet().zCard(RedisConstants.CLASS_IDX_ALL);

        // 1. 获取当前学生位图 (ThreadLocal)
        byte[] studentMap = ThreadLocalUtil.get().scheduleBitmap();

        // 2. Pipeline 执行
        List<Object> pipelineResults = stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String id : classIds) {
                // 命令 1 (索引 i)
                connection.hMGet((RedisConstants.CLASS_INFO_KEY + id).getBytes(),
                        "info".getBytes(), "stock".getBytes());
                // 命令 2 (索引 i+1)
                connection.get((CLASS_SCHEDULE_BIT_MAP + id).getBytes());
            }
            return null;
        });

        // 3. 正确解析：步长为 2
        List<ClassVO> records = new ArrayList<>();
        for (int i = 0; i < pipelineResults.size(); i += 2) {
            // 取出 Hash 结果 (info, stock)
            List<byte[]> hashFields = (List<byte[]>) pipelineResults.get(i);
            // 取出 String 结果 (bitmap)
            byte[] classMap = (byte[]) pipelineResults.get(i + 1);

            if (hashFields == null || hashFields.get(0) == null) continue;

            // 组装 VO
            ClassVO vo = JSON.parseObject(new String(hashFields.get(0)), ClassVO.class);
            if (hashFields.get(1) != null) {
                vo.setStock(Integer.parseInt(new String(hashFields.get(1))));
            }

            // 计算冲突
            vo.setScheduleBitmap(classMap);
            vo.setConflicting(BitMapUtils.isConflicting(studentMap, classMap));

            records.add(vo);
        }
        return new PageResult(total, records);
    }

    /**
     * 选课数据预热逻辑
     */
    @Override
    public void warmUpCache() {
        log.info("选课数据预热：严格按照【ZSet索引 + Hash详情 + String位图】结构");
        List<ClassVO> allClass = courseClassMapper.selectAllClassVO();
        if (allClass == null || allClass.isEmpty()) return;

        // 排除掉不需要进 JSON 的字段
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("stock");
        filter.getExcludes().add("scheduleBitmap");

        stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (ClassVO vo : allClass) {
                String classId = vo.getClassId().toString();

                // 1. 【ZSet】写入分页索引 (Key: class:idx:all)
                connection.zAdd(RedisConstants.CLASS_IDX_ALL.getBytes(),
                        vo.getClassId().doubleValue(),
                        classId.getBytes());

                // 2. 【Hash】写入展示详情 (Key: class:info:{id})
                // 包含：不变量JSON(info) + 频繁读写库存(stock)
                Map<byte[], byte[]> hashFields = new HashMap<>();
                hashFields.put("info".getBytes(), JSON.toJSONString(vo, filter).getBytes());
                hashFields.put("stock".getBytes(), vo.getStock().toString().getBytes());
                connection.hMSet((RedisConstants.CLASS_INFO_KEY + classId).getBytes(), hashFields);

                // 3. 【String】写入课程位图 (Key: class:schedule:{id})
                // 核心：独立存储，才能供 Lua 脚本直接执行 GETBIT 校验冲突
                if (vo.getScheduleBitmap() != null) {
                    String scheduleKey = CLASS_SCHEDULE_BIT_MAP + classId;
                    connection.set(scheduleKey.getBytes(), vo.getScheduleBitmap());
                }
            }
            return null;
        });
        log.info("预热完成，Redis 结构已与表格完全对齐！");
    }

    private PageResult queryFromDb(ClassDTO dto) {
        Page<ClassVO> page = new Page<>(dto.getPage(), dto.getSize());
        courseClassMapper.selectClassPage(page, dto);

        // 获取当前学生的位图，为搜索结果注入冲突状态
        byte[] studentMap = ThreadLocalUtil.get().scheduleBitmap();
        List<ClassVO> records = page.getRecords();

        if (records != null && !records.isEmpty()) {
            records.forEach(vo -> {
                vo.setConflicting(BitMapUtils.isConflicting(studentMap, vo.getScheduleBitmap()));
            });
        }
        return new PageResult(page.getTotal(), records);
    }
    // 针对单门课程的刷新
    @Override
    public void refreshSingleClass(Long classId) {
        // 1. 从数据库查出最新数据
        ClassVO vo = courseClassMapper.selectClassVOById(classId);
        if (vo == null) {
            // 如果数据库没了，直接删缓存
            stringRedisTemplate.delete(Arrays.asList(
                    RedisConstants.CLASS_INFO_KEY + classId,
                    RedisConstants.CLASS_SCHEDULE_BIT_MAP + classId
            ));
            stringRedisTemplate.opsForZSet().remove(RedisConstants.CLASS_IDX_ALL, classId.toString());
            return;
        }

        // 2. 覆盖更新 Redis
        // 这里可以复用 warmUpCache 里的逻辑，但只针对这一个 ID
        stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 更新 Hash
            Map<byte[], byte[]> fields = new HashMap<>();
            fields.put("info".getBytes(), JSON.toJSONString(vo).getBytes());
            fields.put("stock".getBytes(), vo.getStock().toString().getBytes());
            connection.hMSet((RedisConstants.CLASS_INFO_KEY + classId).getBytes(), fields);

            // 更新位图
            if (vo.getScheduleBitmap() != null) {
                connection.set((RedisConstants.CLASS_SCHEDULE_BIT_MAP + classId).getBytes(), vo.getScheduleBitmap());
            }
            return null;
        });
    }
    @Override
    public void clearCache() {
        log.info("开始清空选课系统相关缓存...");
        // 1. 获取所有教学班 ID (从 ZSet 中取)
        Set<String> classIds = stringRedisTemplate.opsForZSet().range(RedisConstants.CLASS_IDX_ALL, 0, -1);

        if (classIds != null && !classIds.isEmpty()) {
            List<String> keysToDelete = new ArrayList<>();
            for (String id : classIds) {
                keysToDelete.add(RedisConstants.CLASS_INFO_KEY + id);
                keysToDelete.add(RedisConstants.CLASS_SCHEDULE_BIT_MAP + id);
            }
            // 2. 批量删除 Hash 和 String 位图
            stringRedisTemplate.delete(keysToDelete);
        }

        // 3. 删除索引 ZSet
        stringRedisTemplate.delete(RedisConstants.CLASS_IDX_ALL);
        log.info("选课相关缓存已清空");
    }

    @Override
    public void book(Long id) {

    }
}