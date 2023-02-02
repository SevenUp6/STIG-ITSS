package com.xjrsoft.common.allenum;

public enum OperateTypeEnum {
    OTHER(0, "其他"),

    LOGIN(1, "登录"),

    EXIT(2, "退出"),

    VISIT(3, "访问"),

    LEAVE(4, "离开"),

    CREATE(5, "新增"),

    DELETE(6, "删除"),

    UPDATE(7, "修改"),

    SUBMIT(8, "提交"),

    EXCEPTION(9, "异常"),

    APPLOGIN(10, "APP登录");

    final int code;
    final String value;

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    private OperateTypeEnum(final int code, final String message) {
        this.code = code;
        this.value = message;
    }
}
