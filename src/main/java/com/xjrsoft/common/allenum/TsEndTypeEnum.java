package com.xjrsoft.common.allenum;

public enum  TsEndTypeEnum {

    NOTIME(1, "无限期"),
    HASTIME(2, "根据时间");

    final int code;
    final String value;

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    private TsEndTypeEnum(final int code, final String message) {
        this.code = code;
        this.value = message;
    }
}
