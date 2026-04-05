package com.lzh.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
