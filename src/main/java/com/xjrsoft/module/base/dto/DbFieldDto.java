package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DbFieldDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 字段名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 数据类型
     */
    @JsonProperty("F_DataType")
    private String dataType;

    /**
     * 说明
     */
    @JsonProperty("F_Remark")
    private String remark;

    @JsonProperty("F_Length")
    private Integer length;
}
