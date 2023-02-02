package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppColumnBtnDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("select")
    private String select;

    @JsonProperty("id")
    private String id;

    @JsonProperty("icon")
    private String icon;
}
