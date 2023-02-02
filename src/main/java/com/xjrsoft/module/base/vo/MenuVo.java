package com.xjrsoft.module.base.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVo implements INode {

    @JsonProperty("meta")
    private JSONObject meta;

    @JsonProperty("target")
    private String target;

    @JsonProperty("path")
    private String urlAddress;

    @JsonProperty("label")
    private String fullName;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("component")
    private String component;

//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("children")
    private List<INode> children;

//    @JsonIgnore
    private String parentId;

//    @JsonIgnore
    private String moduleId;

//    @JsonIgnore
    private String id;

    private String subSystemId;

    @Override
    public String getId() {
        return this.id;
    }

    public void setModuleId(String moduleId) {
        if (meta == null) {
            meta = new JSONObject();
        }
        meta.put("moduleid", moduleId);
        this.moduleId = moduleId;
        this.id = moduleId;
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
