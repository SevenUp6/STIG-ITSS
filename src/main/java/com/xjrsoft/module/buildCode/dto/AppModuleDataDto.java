package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppModuleDataDto {

    @JsonProperty("F_Name")
    private String name;

    @JsonProperty("F_Icon")
    private String icon;

    @JsonProperty("F_Type")
    private String type;

    @JsonProperty("F_SortCode")
    private Integer sortCode;
}
