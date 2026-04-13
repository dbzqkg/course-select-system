package com.lzh.controller.student;

import com.lzh.common.result.Result;
import com.lzh.common.util.JwtUtil;
import com.lzh.common.util.SnowflakeIdUtil;
import com.lzh.pojo.dto.StudentDTO;
import com.lzh.pojo.entity.Student;
import com.lzh.pojo.vo.StudentVO;
import com.lzh.service.LoginService;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Tag(name = "学生端-登录模块", description = "学生认证相关接口")
public class StudentLoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    public Result<String> login(@RequestBody StudentDTO dto) {
        StudentVO vo = loginService.login(dto);
        if(vo!=null){
            String token = JwtUtil.createToken(vo.getId(), vo.getStuId(), vo.getName());
            return Result.success(token);
        }else{
            return Result.error("登录失败，用户名或密码错误");
        }
    }



    @PostMapping("/register")
    public Result<String> register(@RequestBody Student student) {
        // Controller 只管接收成功的结果！如果有异常，根本走不到下一行！
        loginService.register(student);
        return  Result.success();
    }
}
