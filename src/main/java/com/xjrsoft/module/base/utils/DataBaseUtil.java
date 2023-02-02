package com.xjrsoft.module.base.utils;

import com.xjrsoft.core.tool.utils.StringUtil;

public class DataBaseUtil {

    public static final String DB_TYPE_MYSQL = "Mysql";
    public static final String DB_TYPE_SQLSERVER = "SqlServer";
    public static final String DB_TYPE_ORACLE = "Oracle";

    private DataBaseUtil(){}

    public static String buildDbConnUrl(String serverAddr, String port, String dbName, String dbType) {
        String dbUrlTempl = "";
        if (StringUtil.equalsIgnoreCase(DB_TYPE_MYSQL, dbType)) {
            dbUrlTempl = "jdbc:mysql://%1$s:%2$s/%3$s?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%%2B8";
        } else if (StringUtil.equalsIgnoreCase(DB_TYPE_SQLSERVER, dbType)) {
            dbUrlTempl = "jdbc:sqlserver://%1$s:%2$s;DatabaseName=%3$s";
        } else if (StringUtil.equalsIgnoreCase(DB_TYPE_ORACLE, dbType)) {
            dbUrlTempl = "jdbc:oracle:thin:@%1$s:%2$s:%3$s";
        }
        return String.format(dbUrlTempl, serverAddr, port, dbName);
    }
}
