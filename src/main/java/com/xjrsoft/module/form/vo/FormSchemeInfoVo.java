package com.xjrsoft.module.form.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormSchemeInfoVo {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 表单名字
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 表单类型0自定义表单,1自定义表单（OA），2系统表单
     */
    @JsonProperty("F_Type")
    private Integer type;

    /**
     * 表单分类
     */
    @JsonProperty("F_Category")
    private String category;

    /**
     * 当前模板主键
     */
    @JsonProperty("F_SchemeId")
    private String schemeId;

    /**
     * 状态1启用0未启用2草稿
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    /**
     * 创建时间
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建用户id
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建用户名字
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

//    /**
//     * 表单地址
//     */
//    @JsonProperty("F_UrlAddress")
//    private String urlAddress;

    @JsonProperty("F_DesignType")
    private Integer designType;
}
