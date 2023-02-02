package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanySimpleInfoVo {

    /**
     * 公司主键
     */
    @JsonProperty("F_CompanyId")
    private String companyId;

    /**
     * 公司分类
     */
    @JsonProperty("F_Category")
    private Integer category;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 公司代码
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 公司简称
     */
    @JsonProperty("F_ShortName")
    private String shortName;

    /**
     * 公司名称
     */
    @JsonProperty("F_FullName")
    private String fullName;
}
