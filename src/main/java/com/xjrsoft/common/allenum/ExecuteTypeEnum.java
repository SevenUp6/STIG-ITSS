package com.xjrsoft.common.allenum;

public enum ExecuteTypeEnum {

    ONLYONE(1, "只执行一次"),
    SIMPLE(2, "简单重复执行"),
    FREQUENCY(3, "频率"),
    CORN(4, "corn表达式");

    final int code;
    final String value;

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    private ExecuteTypeEnum(final int code, final String message) {
        this.code = code;
        this.value = message;
    }
}
