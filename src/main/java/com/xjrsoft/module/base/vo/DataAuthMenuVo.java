package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataAuthMenuVo implements INode {

    @JsonProperty("F_ModuleId")
    private String moduleId;

    @JsonProperty("F_ParentId")
    private String parentId;

    @JsonProperty("F_Target")
    private String target;

    @JsonProperty("F_UrlAddress")
    private String urlAddress;

    @JsonProperty("F_FullName")
    private String fullName;

    @JsonProperty("F_EnCode")
    private String enCode;

    @JsonProperty("F_Icon")
    private String icon;

    @JsonProperty("F_Component")
    private String component;

    @JsonProperty("children")
    private List<INode> children;

    @JsonProperty("authorizedData")
    private DataAuthorizedVo dataAuthorizedVo;

    @Override
    public String getId() {
        return this.moduleId;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public List<INode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return this.children;
    }
}
