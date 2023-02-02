package com.xjrsoft.module.base.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DataSourceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 编号
     */
    @JsonProperty("F_Code")
    private String code;

    /**
     * 名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 数据库主键
     */
    @JsonProperty("F_DbId")
    private String dbId;

    /**
     * 数据库连接名称
     */
    @JsonProperty("F_DbName")
    private String dbName;

    /**
     * sql语句
     */
    @JsonProperty("F_Sql")
    private String fsql;

    /**
     * b备注
     */
    @JsonProperty(value = "F_Description")
    private String description;

    /**
     * 创建人名字
     */
    @JsonProperty(value = "F_CreateUserName")
    private String createUserName;

    /**
     * 创建日期
     */
    @JsonProperty(value = "F_CreateDate")
    private LocalDateTime createDate;


}

