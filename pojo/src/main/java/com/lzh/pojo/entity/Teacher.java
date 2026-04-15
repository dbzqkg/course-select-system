package com.lzh.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教师实体类
 */
@Data
@TableName("teacher")
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 教师工号
     */
    private String teacherNo;

    /**
     * 教师姓名
     */
    private String name;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 插入和更新时都会自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    // 插入和更新时自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}
