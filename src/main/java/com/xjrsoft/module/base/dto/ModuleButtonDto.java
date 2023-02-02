package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleButtonDto {
    private static final long serialVersionUID = 1L;

    /**
     * 按钮主键
     */
    @JsonProperty("F_ModuleButtonId")
    private String moduleButtonId;

    /**
     * 功能主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 图标
     */
    @JsonProperty("F_Icon")
    private String icon;

    /**
     * 编码
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 名称
     */
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * Action地址
     */
    @JsonProperty("F_ActionAddress")
    private String actionAddress;

    /**
     * 排序码
     */
    @JsonProperty("$index")
    private Integer sortCode;
}
