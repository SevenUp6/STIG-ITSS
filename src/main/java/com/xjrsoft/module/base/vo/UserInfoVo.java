package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoVo {

    @JsonProperty("UserInfo")
    private UserSimpleInfoVo userSimpleInfoVo;

    @JsonProperty("CompanyInfo")
    private CompanySimpleInfoVo companySimpleInfoVo;

    @JsonProperty("DepartmentInfo")
    private List<DepartmentSimpleVo> departmentSimpleVoList;
}
