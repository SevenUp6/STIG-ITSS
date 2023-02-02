package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleButtonVo implements IModuleVo {
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

    @JsonProperty("children")
    private List<INode> children;

    @Override
    public String getId() {
        return this.moduleButtonId;
    }

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return children;
    }
}
