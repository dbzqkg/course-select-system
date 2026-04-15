package com.lzh.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生选课记录实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("student_class_record")
public class StudentClassRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学生学号
     */
    private String stuId;

    /**
     * 课程班级ID（关联course_class表）
     */
    private Long classId;

    /**
     * 课程ID（关联course表）
     */
    private Long courseId;

    /**
     * 课程学分
     */
    private BigDecimal credit;

    /**
     * 选课状态：默认1-已选，可自定义枚举（如0待审核、2退课）
     */
    private Integer status;

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
