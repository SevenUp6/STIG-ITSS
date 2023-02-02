package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleTreeEntityVo {

    @JsonProperty("F_ModuleId")
    private String moduleId;

    @JsonProperty("F_ParentId")
    private String parentId;

    @JsonProperty("F_ParentModule")
    private ModuleTreeEntityVo parentModule;

    @JsonProperty("F_EnCode")
    private String enCode;

    @JsonProperty("F_FullName")
    private String fullName;

    @JsonProperty("F_Icon")
    private String icon;

    @JsonProperty("component")
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

    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    @JsonProperty("F_SortCode")
    private Integer sortCode;

    public void buildFullUrlAddress(StringBuilder urlSb) {
        urlSb.insert(0, this.getUrlAddress());
        if (parentModule != null) {
            parentModule.buildFullUrlAddress(urlSb);
        }
    }
}
