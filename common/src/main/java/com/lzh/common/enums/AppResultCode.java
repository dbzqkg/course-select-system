package com.lzh.common.enums;

public enum AppResultCode {
    SUCCESS(200, "操作成功"),
    REPEAT_BOOK(1001, "您已经预选过该课程"),
    DB_WRITE_ERROR(5001, "系统繁忙，预选记录保存失败"),
    PARAM_ERROR(4001, "参数非法");

    private final Integer code;
    private final String message;

    AppResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}