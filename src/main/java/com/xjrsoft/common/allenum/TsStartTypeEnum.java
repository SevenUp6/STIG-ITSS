package com.xjrsoft.common.allenum;

public enum  TsStartTypeEnum {

    RUN(1, "立即执行"),
    TIME(2, "根据开始时间");

    final int code;
    final String value;

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    private TsStartTypeEnum(final int code, final String message) {
        this.code = code;
        this.value = message;
    }
}
