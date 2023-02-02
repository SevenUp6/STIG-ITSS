package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleColumnVo implements IModuleVo {
    private static final long serialVersionUID = 1L;

    /**
     * 列主键
     */
    @JsonProperty("F_ModuleColumnId")
    private String moduleColumnId;

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
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    @JsonProperty("children")
    private List<INode> children;

    @Override
    public List<INode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    @Override
    public String getId() {
        return null;
    }
}
