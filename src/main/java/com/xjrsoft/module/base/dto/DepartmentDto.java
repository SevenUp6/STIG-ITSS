package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DepartmentDto {
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
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("postUserJson")
    private Map<String, List<String>> postUserJson;
}
