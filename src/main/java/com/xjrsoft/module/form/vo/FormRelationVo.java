package com.xjrsoft.module.form.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormRelationVo {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 表单主键
     */
    @JsonProperty("F_FormId")
    private String formId;

    /**
     * 功能主键
     */
    @JsonProperty("F_ModuleId")
    @JsonIgnore
    private String moduleId;

    /**
     * 功能主键
     */
    @JsonProperty("F_FullName")
    private String moduleName;

    /**
     * 功能主键
     */
    @JsonProperty("F_Name")
    private String formName;


    @JsonProperty("F_Module")
    private ModuleFormVo module;

    /**
     * 创建时间
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建人Id
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建人名称
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    /**
     * 发布的设置信息
     */
    @JsonProperty("F_SettingJson")
    private String settingJson;
}
