package com.lzh.common.context;

/**
 * 当前登录用户信息封装 (Java 17 Record 语法)
 */
public record UserContext(Long id, String stuId, String name) {
}