package com.lzh.controller.student;

import com.lzh.common.result.PageResult;
import com.lzh.common.result.Result;
import com.lzh.pojo.dto.ClassDTO;
import com.lzh.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/course")
@Tag(name = "学生端-选课模块", description = "选课相关接口")
public class StudentClassController {
    @Autowired
    private ClassService classService;

    @PostMapping("/page")
    public Result<PageResult> page(@RequestBody ClassDTO dto) {
        PageResult pageResult = classService.page(dto);
        return Result.success(pageResult);
    }

    @PostMapping("/book/{id}")
    @Operation(summary = "学生端-选课意向添加")
    public Result<String> book(@PathVariable Long id) {
        classService.book(id);
        return Result.success("选课意向已提交");
    }
}
