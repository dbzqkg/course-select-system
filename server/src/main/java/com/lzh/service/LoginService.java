package com.lzh.service;

import com.lzh.common.result.Result;
import com.lzh.pojo.dto.StudentDTO;
import com.lzh.pojo.entity.Student;
import com.lzh.pojo.vo.StudentVO;
import org.springframework.stereotype.Service;


public interface LoginService {
    StudentVO login(StudentDTO dto);
}
