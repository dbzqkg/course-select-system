package com.lzh.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
    /**
     * 分页查询
     */
    private Long page = 1L;
    private Long size = 10L;

    /**
     * 查询条件
     */
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 课程编号
     */
    private String courseNo;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程类型（1:必修 2:选修 等）
     */
    private Integer type;

    /**
     * 学分分类
     */
    private Integer subType;
}
