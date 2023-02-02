package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.List;

@Data
public class ModuleFormVo implements IModuleVo {
    private static final long serialVersionUID = 1L;

    /**
     * 列主键
     */
    @JsonProperty("F_ModuleFormId")
    private String moduleFormId;

    /**
     * 功能主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

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

    @Override
    public String getId() {
        return this.moduleFormId;
    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public List<INode> getChildren() {
        return null;
    }
}
