package com.lzh.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("student_wishlist")
public class StudentWishlist {
    /** 主键ID */
    private Long id;

    /** 学生逻辑ID */
    private Long studentId;

    /** 教学班ID */
    private Long classId;

    /** 志愿优先级: 1-5等 */
    private Integer priority;

    /** 状态: 0-待开奖, 1-中签, 2-未中签 */
    private Integer status;

    // ============= 审计字段开始 =============

    /** 创建人 (存学号或"SYSTEM") */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 更新人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}