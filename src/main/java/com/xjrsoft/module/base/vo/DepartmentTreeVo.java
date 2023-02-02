package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DepartmentTreeVo implements INode {
    private static final long serialVersionUID = 1L;

    /**
     * 部门主键
     */
    @JsonProperty("F_DepartmentId")
    private String departmentId;

    /**
     * 公司主键
     */
    @JsonProperty("F_CompanyId")
    private String companyId;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 部门代码
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 部门名称
     */
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * 部门简称
     */
    @JsonProperty("F_ShortName")
    private String shortName;

    /**
     * 部门类型
     */
    @JsonProperty("F_Nature")
    private String nature;

    /**
     * 负责人
     */
    @JsonProperty("F_Manager")
    private String manager;

    /**
     * 外线电话
     */
    @JsonProperty("F_OuterPhone")
    private String outerPhone;

    /**
     * 内线电话
     */
    @JsonProperty("F_InnerPhone")
    private String innerPhone;

    /**
     * 电子邮件
     */
    @JsonProperty("F_Email")
    private String email;

    /**
     * 部门传真
     */
    @JsonProperty("F_Fax")
    private String fax;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("children")
    private List<INode> children;

    @JsonIgnore
    @Override
    public String getId() {
        return this.departmentId;
    }

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }
}
