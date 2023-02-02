package com.xjrsoft.common.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String MSG_QUERY_SUCCESSFUL = "查询成功！";

    private int code;

    private boolean success;

    private String msg;

    private T data;

    public static <T> Response<T> ok(T data) {
        return ok(0, data ,"");
    }

    public static <T> Response<T> ok(T data, String msg) {
        return ok(0, data, msg);
    }

    //
    public static <T> Response ok() {
        return ok(0, null, "");
    }

    public static <T> Response notOk() {
        return new Response(ResultCode.INTERNAL_SERVER_ERROR.code, null, "");
    }

    //成功 自定义返回 code  data  以及  msg
    public static <T> Response ok(int code, T data, String msg) {
        return new Response(code, data,msg);
//        return new Response(code, data, data == null ? "暂无承载数据" : msg);
    }
    //错误  返回自定义返回
    public static <T> Response<T> notOk(int code,  String msg) {
        return new Response(code, null, msg);
    }
    //错误  返回500
    public static <T> Response<T> notOk(String msg) {
        return new Response(ResultCode.INTERNAL_SERVER_ERROR.code, null, msg);
    }

    public static <T> Response<T> status(boolean flag) {
        return flag ? ok(null,"操作成功") : notOk("操作失败");
    }

    private Response(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.code == code;
    }

    private Response(boolean success) {
        this.success = success;
    }
}
