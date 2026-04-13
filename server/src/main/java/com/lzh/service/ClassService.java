package com.lzh.service;

import com.lzh.common.result.PageResult;
import com.lzh.pojo.dto.ClassDTO;


public interface ClassService {
    PageResult page(ClassDTO dto);

    void warmUpCache();

    // 针对单门课程的刷新
    void refreshSingleClass(Long classId);

    // 在 ClassServiceImpl.java 中实现
    void clearCache();
}
