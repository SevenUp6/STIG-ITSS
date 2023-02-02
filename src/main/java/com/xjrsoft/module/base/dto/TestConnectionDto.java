package com.xjrsoft.module.base.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TestConnectionDto implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 服务器地址
     */
    @JsonProperty("F_ServerAddress")
//    private String serverAddress;
    private String F_ServerAddress;

    /**
     * 数据库名称
     */
    @JsonProperty("F_DBName")
//    private String dbName;
    private String F_DBName;

    /**
     * 数据库别名
     */
    @JsonProperty("F_DBAlias")
//    private String dbAlias;
    private String F_DBAlias;

    /**
     * 数据库类型
     */
    @JsonProperty("F_DbType")
//    private String dbType;
    private String F_DbType;

    /**
     * 数据库版本
     */
    @JsonProperty("F_Version")
//    private String version;
    private String F_Version;

    /**
     * 连接地址
     */
    @JsonProperty("F_DbConnection")
//    private String dbConnection;
    private String F_DbConnection;

    /**
     * 连接数据库密码
     */
    @JsonProperty("F_DBPwd")
//    private String dbPwd;
    private String F_DBPwd;

    /**
     * 连接数据库用户名
     */
    @JsonProperty("F_DBUserName")
//    private String dbUserName;
    private String F_DBUserName;
}