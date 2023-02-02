package com.xjrsoft.core.tool.constant;

public interface XjrConstant {
    String UTF_8 = "UTF-8";
    String CONTENT_TYPE_NAME = "Content-type";
    String CONTENT_TYPE = "application/json;charset=utf-8";
    String SECURITY_ROLE_PREFIX = "ROLE_";
    String DB_PRIMARY_KEY = "id";
    int DB_STATUS_NORMAL = 1;
    int DB_NOT_DELETED = 0;
    int DB_IS_DELETED = 1;
    int DB_ADMIN_NON_LOCKED = 0;
    int DB_ADMIN_LOCKED = 1;
    String ADMIN_TENANT_ID = "000000";
    String LOG_NORMAL_TYPE = "1";
    String DEFAULT_NULL_MESSAGE = "暂无承载数据";
    String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    String DEFAULT_FAILURE_MESSAGE = "操作失败";
    String DEFAULT_UNAUTHORIZED_MESSAGE = "签名认证失败";
}
