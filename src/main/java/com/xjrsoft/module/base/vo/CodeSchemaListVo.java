package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CodeSchemaListVo {
    /**
     * 代码模板主键
     */
    @JsonProperty("F_Id")
    private String id;
    /**
     * 模板名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 模板类型
     */
    @JsonProperty("F_Type")
    private String type;

    /**
     * 行业类型
     */
    @JsonProperty("F_Catalog")
    private String catalog;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
