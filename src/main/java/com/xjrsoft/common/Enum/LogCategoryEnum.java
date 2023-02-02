package com.xjrsoft.common.Enum;

public enum  LogCategoryEnum {

    LOGIN(1, "登录"),

    GET(2, "访问"),

    OPERAT(3, "操作"),

    EXCEPTION(4, "异常");

    final int code;
    final String value;

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    private LogCategoryEnum(final int code, final String message) {
        this.code = code;
        this.value = message;
    }
}
