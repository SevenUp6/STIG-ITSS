package com.xjrsoft.module.base.vo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CodeSchemaVo implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 代码Schema
     */
    @JsonProperty("F_CodeSchema")
    private String codeSchema;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建人ID
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建人姓名
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;
}
