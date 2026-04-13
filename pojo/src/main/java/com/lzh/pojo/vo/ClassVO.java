package com.lzh.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassVO {
    // 1. 核心逻辑ID（前端点击抢课发给后端的唯一凭证）
    private Long classId;

    // 2. 课程基础信息 (来自 course 表)
    private String courseNo;
    private String courseName;
    private BigDecimal credit;
    private Integer type;

    // 3. 教师信息 (来自 teacher 表)
    private String teacherName;

    // 4. 教学班具体信息 (来自 course_class 表)
    private String timeLocationInfo;
    private Integer capacity;
    private Integer stock;
    private byte[] scheduleBitmap;

    // 5.前端展示
    private Boolean conflicting;
}
