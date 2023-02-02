package com.xjrsoft.module.form.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModuleFormVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("F_ModuleId")
    private String moduleId;

    @JsonProperty("F_ParentId")
    private String parentId;

    @JsonProperty("F_EnCode")
    private String enCode;

    @JsonProperty("F_FullName")
    private String fullName;

    @JsonProperty("F_Icon")
    private String icon;

    @JsonProperty("F_Component")
    private String component;

    @JsonProperty("F_UrlAddress")
    private String urlAddress;

    @JsonProperty("F_Target")
    private String target;

    @JsonProperty("F_IsMenu")
    private Integer isMenu;

    @JsonProperty("F_AllowExpand")
    private Integer allowExpand;

    @JsonProperty("F_IsPublic")
    private Integer isPublic;

    @JsonProperty("F_AllowEdit")
    private Integer allowEdit;

    @JsonProperty("F_AllowDelete")
    private Integer allowDelete;

    @JsonProperty("F_SortCode")
    private Integer sortCode;

    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    @JsonProperty("F_SubSystemId")
    private String subSystemId;

    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    @JsonProperty("F_CreateUserId")
    private String createUserId;

    @JsonProperty("F_CreateUserName")
    private String createUserName;

    @JsonProperty("F_ModifyDate")
    private LocalDateTime modifyDate;

    @JsonProperty("F_ModifyUserId")
    private String modifyUserId;

    @JsonProperty("F_ModifyUserName")
    private String modifyUserName;
}
