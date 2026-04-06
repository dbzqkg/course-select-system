package com.lzh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 选课系统主启动类 (全村唯一的希望)
 */
@SpringBootApplication
// 【核心注意】：一定要加上这个注解，告诉 MyBatis-Plus 你的 Mapper 接口在哪里！
// 否则启动后调数据库会报 Invalid bound statement (not found)
@MapperScan("com.lzh.mapper")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}