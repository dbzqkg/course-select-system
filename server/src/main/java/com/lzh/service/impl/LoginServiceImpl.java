package com.lzh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.common.constants.RedisConstants;
import com.lzh.common.exception.BusinessException;
import com.lzh.common.result.Result;
import com.lzh.common.util.JwtUtil;
import com.lzh.common.util.SnowflakeIdUtil;
import com.lzh.mapper.LoginMapper;
import com.lzh.pojo.dto.StudentDTO;
import com.lzh.pojo.entity.Student;
import com.lzh.pojo.vo.StudentVO;
import com.lzh.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Override
//    public StudentVO login(StudentDTO dto) {
//        // 1. 先根据【学号】去数据库把这个人捞出来
//        Student student = loginMapper.selectByStuId(dto.getStuId());
//
//        // 2. 如果没这个人，直接返回 null
//        if (student == null) {
//            return null;
//        }
//
//        String md5Password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());
//
//        // 3. 用MD5密后的密文，和数据库里的密文进行比对！
//        if (!student.getPassword().equals(md5Password)) {
//            return null; // 密码错误
//        }
//
//        // 4. 密码正确，封装 VO
//        StudentVO vo = new StudentVO();
//        vo.setId(student.getId());
//        vo.setStuId(student.getStuId());
//        vo.setName(student.getName());
//        return vo;
//    }
// 修改 LoginServiceImpl.java 的 login 方法
@Override
public StudentVO login(StudentDTO dto) {
    Student student = loginMapper.selectByStuId(dto.getStuId());
    if (student == null || !student.getPassword().equals(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()))) {
        return null;
    }

    // --- 新增：同步写入 Redis 位图 ---
    byte[] bitmap = student.getScheduleBitmap();
    if (bitmap == null) bitmap = new byte[128]; // 1024位全0

    byte[] finalBitmap = bitmap;
    stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
        connection.setEx((RedisConstants.STUDENT_SCHEDULE_BIT + student.getId()).getBytes(),
                JwtUtil.EXPIRATION_TIME / 1000,
                finalBitmap);
        return null;
    });

    StudentVO vo = new StudentVO();
    vo.setId(student.getId());
    vo.setStuId(student.getStuId());
    vo.setName(student.getName());
    return vo;
}

    @Override
    public void register(Student student) {
        if (student == null || student.getStuId() == null || student.getPassword() == null) {
            throw new BusinessException("数据不合法，学号或密码为空");
        }

        Student existStu = loginMapper.selectByStuId(student.getStuId());
        if (existStu != null) {
            throw new BusinessException("学号已被注册");
        }

        student.setPassword(DigestUtils.md5DigestAsHex(student.getPassword().getBytes()));
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        student.setCreateBy(student.getStuId());
        student.setUpdateBy(student.getStuId());
        loginMapper.insert(student);

    }
}
