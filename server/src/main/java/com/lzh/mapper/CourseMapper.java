package com.lzh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.pojo.entity.Course;
import com.lzh.pojo.entity.CourseClass;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}
