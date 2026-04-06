package com.lzh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.pojo.dto.StudentDTO;
import com.lzh.pojo.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper extends BaseMapper<Student> {

}
