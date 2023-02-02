package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class AuthorizeDataDto {

    @JsonProperty("F_ModuleIds")
    private Set<String> moduleIdList;

    @JsonProperty("F_ObjectId")
    private String objectId;

    @JsonProperty("F_ObjectType")
    private Integer objectType;

    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    @JsonProperty("F_EnabledChildrenMark")
    private Integer enabledChildrenMark;

    @JsonProperty("F_DataSettingType")
    private Integer dataSettingType;
}
