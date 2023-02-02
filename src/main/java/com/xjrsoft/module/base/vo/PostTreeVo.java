package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostTreeVo implements INode {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_PostId")
    private String postId;

    /**
     * 上级Id
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 岗位名称
     */
    @JsonProperty("F_Name")
    private String name;

    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 公司主键
     */
    @JsonProperty("F_CompanyId")
    private String companyId;

    /**
     * 部门主键
     */
    @JsonProperty("F_DepartmentId")
    private String departmentId;

    /**
     * 部门名称
     */
    @JsonProperty("F_DepartmentName")
    private String departmentName;

    @JsonProperty("F_ParentName")
    private String parentName;
    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("children")
    private List<INode> children;

    @Override
    @JsonIgnore
    public String getId() {
        return this.postId;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }
}
