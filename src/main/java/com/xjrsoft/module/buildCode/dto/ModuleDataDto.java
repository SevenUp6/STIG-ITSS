package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleDataDto {

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

    @JsonProperty("F_IsMenu")
    private Integer isMenu;

    @JsonProperty("F_IsPublic")
    private Integer isPublic;

    @JsonProperty("F_SortCode")
    private Integer sortCode;

    @JsonProperty("F_SubSystemId")
    private String subSystemId;
}
