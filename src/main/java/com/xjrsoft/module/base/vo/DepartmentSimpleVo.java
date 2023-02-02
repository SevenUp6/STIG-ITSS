package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DepartmentSimpleVo {

    @JsonProperty("F_DepartmentId")
    private String departmentId;

    @JsonProperty("F_DepartmentName")
    private String departmentName;

    @JsonProperty("F_EnCode")
    private String enCode;
}
