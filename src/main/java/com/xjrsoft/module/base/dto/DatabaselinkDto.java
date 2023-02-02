package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DatabaselinkDto implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 服务器地址
     */
    @JsonProperty("F_ServerAddress")
    private String serverAddress;

    /**
     * 数据库名称
     */
    @JsonProperty("F_DBName")
    private String dbName;

    /**
     * 数据库别名
     */
    @JsonProperty("F_DBAlias")
    private String dbAlias;

    /**
     * 数据库类型
     */
    @JsonProperty("F_DbType")
    private String dbType;

    /**
     * 数据库版本
     */
    @JsonProperty("F_Version")
    private String version;

    /**
     * 连接地址
     */
    @JsonProperty("F_DbConnection")
    private String dbConnection;

    /**
     * 连接数据库密码-
     */
    @JsonProperty("F_DBPwd")
    private String dbPwd;

    /**
     * 连接数据库用户名
     */
    @JsonProperty("F_DBUserName")
    private String dbUserName;

    /**
     * 描述
     */
    @JsonProperty("F_Description")
    private String description;
}
