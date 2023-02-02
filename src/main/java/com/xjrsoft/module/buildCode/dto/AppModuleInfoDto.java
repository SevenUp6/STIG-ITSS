package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppModuleInfoDto {

    @JsonProperty("F_Name")
    private String name;

    @JsonProperty("F_Type")
    private String type;

    @JsonProperty("F_Icon")
    private String icon;

    @JsonProperty("F_SortCode")
    private Integer sortCode;
}
