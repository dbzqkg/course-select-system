package com.lzh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.pojo.dto.ClassDTO;

import com.lzh.pojo.entity.CourseClass;
import com.lzh.pojo.vo.ClassVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseClassMapper extends BaseMapper<CourseClass> {
    // Page 放第一位，DTO 放第二位
    IPage<ClassVO> selectClassPage(Page<ClassVO> page, @Param("dto") ClassDTO dto);


    List<ClassVO> selectAllClassVO();

    ClassVO selectClassVOById(Long classId);
}
