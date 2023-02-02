package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpecialPostUserVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("F_Account")
    private String account;

    @JsonProperty("F_CompanyName")
    private String companyName;

    @JsonProperty("F_DepartmentName")
    private String departmentName;

    @JsonProperty("F_ObjectId")
    private String objectId;

    @JsonProperty("F_RealName")
    private String realName;

    @JsonProperty("F_UserId")
    private String userId;
}
