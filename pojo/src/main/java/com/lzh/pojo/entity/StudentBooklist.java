package com.lzh.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 预选表
* @TableName student_booklist
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("student_booklist")
public class StudentBooklist implements Serializable {

    /**
    * 
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
    * 
    */

    private Long studentId;
    /**
    * 
    */

    private Long classId;
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
