package com.lzh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.common.result.Result;
import com.lzh.mapper.LoginMapper;
import com.lzh.pojo.dto.StudentDTO;
import com.lzh.pojo.entity.Student;
import com.lzh.pojo.vo.StudentVO;
import com.lzh.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public StudentVO login(StudentDTO dto) {
        // 1. 先根据【学号】去数据库把这个人捞出来
        Student student = loginMapper.selectOne(
                new LambdaQueryWrapper<Student>()
                        .eq(Student::getStuId, dto.getStuId())
        );

        // 2. 如果没这个人，直接返回 null
        if (student == null) {
            return null;
        }

        String md5Password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());

        // 3. 用MD5密后的密文，和数据库里的密文进行比对！
        if (!student.getPassword().equals(md5Password)) {
            return null; // 密码错误
        }

        // 4. 密码正确，封装 VO
        StudentVO vo = new StudentVO();
        vo.setId(student.getId());
        vo.setStuId(student.getStuId());
        vo.setName(student.getName());
        return vo;
    }
}
