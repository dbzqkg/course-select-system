package com.lzh.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
    /**
     * 学号
     */
    private String stuId;

    /**
     * 密码
     */
    private String password;//M5D

}
