package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SpecialPostVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("F_Id")
    private String id;

    @JsonProperty("F_Name")
    private String name;

    @JsonProperty("F_Remark")
    private String remark;

    @JsonProperty("F_Sort")
    private Integer sort;

    @JsonProperty("F_Type")
    private Integer type;

    @JsonProperty("F_Temp1")
    private String temp1;

    @JsonProperty("F_Temp2")
    private String temp2;

    @JsonProperty("children")
    private List<SpecialPostUserVo> children;
}
