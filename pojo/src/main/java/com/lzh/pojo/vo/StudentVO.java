package com.lzh.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 姓名
     */
    private String name;
}
