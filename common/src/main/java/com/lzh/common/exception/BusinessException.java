package com.lzh.common.exception;

/**
 * 自定义业务异常
 */


import com.lzh.common.enums.AppResultCode;
import lombok.Getter;

@Getter // 必须加 Getter，这样全局异常处理器才能拿到 code
public class BusinessException extends RuntimeException {

    private final Integer code;

    // 重点：构造函数接收我们定义的枚举
    public BusinessException(AppResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}