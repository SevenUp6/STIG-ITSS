package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataSourceDto {

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
     * sql语句
     */
    @JsonProperty("F_Sql")
    private String fsql;

    /**
     * b备注
     */
    @JsonProperty(value = "F_Description")
    private String description;
}
