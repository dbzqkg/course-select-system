package com.lzh.controller.admin;

import com.lzh.common.result.Result;
import com.lzh.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/class")
@Tag(name = "管理员端-课程班模块", description = "管理员对选课的crud与预热")
public class AdminClassController {
    @Autowired
    private ClassService classService;

    @PostMapping("/warmup")
    @Operation(summary = "预热选课数据", description = "从数据库同步教学班信息、库存及位图到 Redis")
    public Result<String> warmUp() {
        classService.warmUpCache();
        return Result.success("预热任务执行成功");
    }

    @PutMapping("/refresh/{id}")
    @Operation(summary = "刷新单个课程缓存" , description = "根据教学班ID刷新 Redis 中的库存和位图数据")
    public Result<String> refreshCache(@PathVariable("id") Long classId) {
        classService.refreshSingleClass(classId);
        return Result.success("课程缓存刷新成功");
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清除课程缓存", description = "删除 Redis 中所有课程相关的库存和位图数据")
    public Result<String> clearCache() {
        classService.clearCache();
        return Result.success("课程缓存清除成功");
    }
}
