package com.xjrsoft.common.result;


public enum ResultCode implements IResultCode {
    SUCCESS(0, "操作成功"),
    FAILURE(10400, "业务异常"),
    UN_AUTHORIZED(10401, "请求未授权"),
    NOT_FOUND(10404, "404 没找到请求"),
    MSG_NOT_READABLE(10400, "消息不能读取"),
    METHOD_NOT_SUPPORTED(10405, "不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED(10415, "不支持当前媒体类型"),
    REQ_REJECT(10403, "请求被拒绝"),
    INTERNAL_SERVER_ERROR(10500, "服务器异常"),
    PARAM_MISS(10400, "缺少必要的请求参数"),
    PARAM_TYPE_ERROR(10400, "请求参数类型错误"),
    PARAM_BIND_ERROR(10400, "请求参数绑定错误"),
    PARAM_VALID_ERROR(10400, "参数校验失败");

    final int code;
    final String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}