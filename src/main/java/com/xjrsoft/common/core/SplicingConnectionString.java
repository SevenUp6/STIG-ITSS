package com.xjrsoft.common.core;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @title 根据输入的参数获取连接字符串
 * @author tzx
 * @create 2020年11月7日 13:28:06
* */
public class SplicingConnectionString {

    /**
     * @title 获取连接字符串
     * @param addr ip地址
     * @param dbName 数据库名
     * @param dbType 数据库类型 （使用mybatis-plus的DbType枚举）
     * @param port 端口
     * */
    public static String getConnectionString(DbType dbType,String addr,String port,String dbName){
        switch (dbType){
            case MYSQL:
                return "jdbc:mysql://" + addr + ":" + port + "/" + dbName + "?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC";//注意: 高版本的 mysql 需要显示指定 useSSL
            case SQL_SERVER:
                return "jdbc:sqlserver://" + addr +":" + port +"; DatabaseName=" + dbName;
            case ORACLE:
                return "jdbc:oracle:thin:@" + addr + ":" + port + ":" + dbName;
            case POSTGRE_SQL:
                return "jdbc:postgresql://" + addr + "/" + dbName;
            case DM:
                return "jdbc:dm://" + addr + ":" + port + ":" + dbName;
            default:
                return "";//注意: 高版本的 mysql 需要显示指定 useSSL
        }
    }


    /**
     * @title 获取数据库驱动字符串
     * @param dbType 数据库类型 （使用mybatis-plus的DbType枚举）
     * */
    public static String getDriverString(DbType dbType){
        switch (dbType){
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";//注意: 高版本的 mysql
            case SQL_SERVER:
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case ORACLE:
            case ORACLE_12C:
                return "oracle.jdbc.driver.OracleDriver";
            case POSTGRE_SQL:
                return "org.postgresql.Driver";
            case DM:
                return "dm.jdbc.driver.DmDriver";
            default:
                return "";//注意: 高版本的 mysql
        }
    }



}
