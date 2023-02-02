package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostDto {
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
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
