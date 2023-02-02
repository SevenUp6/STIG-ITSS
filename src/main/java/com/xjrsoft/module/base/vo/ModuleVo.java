package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleVo implements INode, Serializable {
    private static final long serialVersionUID = 1L;

//    @JsonIgnore
    @JsonProperty("F_ModuleId")
    private String moduleId;

//    @JsonIgnore
    @JsonProperty("F_ParentId")
    private String parentId;

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

    @JsonProperty("children")
    private List<INode> children;

    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    @JsonProperty("F_SubSystemId")
    private String subSystemId;

    @JsonProperty("F_SortCode")
    private Integer sortCode;

    @Override
    public String getId() {
        return this.moduleId;
    }

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }
}
