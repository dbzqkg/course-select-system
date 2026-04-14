package com.lzh.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("student")
public class Student {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 专业
     */
    private String major;
    /**
     * 时间位图
     */
    private byte[] scheduleBitmap;

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
