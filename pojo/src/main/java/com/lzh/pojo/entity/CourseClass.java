package com.lzh.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程班级（开课班）实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("course_class")
public class CourseClass {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联课程ID
     */
    private Long courseId;

    /**
     * 关联教师ID
     */
    private Long teacherId;

    /**
     * 课程学分
     */
    private BigDecimal credit;

    /**
     * 时间地点信息（如：周一1-2节 教一-101）
     */
    private String timeLocationInfo;

    /**
     * 排课位图（二进制存储排课时间）
     */
    private byte[] scheduleBitmap;

    /**
     * 班级容量
     */
    private Integer capacity;

    /**
     * 剩余可选名额
     */
    private Integer stock;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

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

    /** add*/
    /**
     * 实际已选人数 (中签或抢占成功的最终人数)
     * 对应数据库 actual_count
     */
    private Integer actualCount;

    /**
     * 当前报了意向的总人数 (仅用于选修课展示热度，即有多少人选了这门课做志愿)
     * 对应数据库 wish_count
     */
    private Integer wishCount;
}
