package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SchemeInfoDto {
    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String Id;

    /**
     * 表单名字
     */
    @JsonProperty("F_Name")
    private String Name;

    /**
     * 表单类型0自定义表单,1自定义表单（OA），2系统表单
     */
    @JsonProperty("F_Type")
    private Integer Type;

    /**
     * 表单分类
     */
    @JsonProperty("F_Category")
    private String Category;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String Description;

    /**
     * 表单地址
     */
    @JsonProperty("F_UrlAddress")
    private String UrlAddress;
}
