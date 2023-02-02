package com.xjrsoft.common.allenum;

public enum TsStateEnum {

    NOTRUN(1, "未执行"),
    RUN(2, "运行中"),
    STOP(3, "暂停"),
    END(4, "结束"),
    CLOSE(4, "已关闭");

    final int code;
    final String value;

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    private TsStateEnum(final int code, final String message) {
        this.code = code;
        this.value = message;
    }
}
