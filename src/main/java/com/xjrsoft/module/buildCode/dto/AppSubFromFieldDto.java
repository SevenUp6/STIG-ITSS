package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class AppSubFromFieldDto {

    @JsonProperty("dataSource")
    private String dataSource;

    @JsonProperty("dataItem")
    private String dataItem;

    @JsonProperty("showfield")
    private String showField;

    @JsonProperty("savefield")
    private String saveField;

    @JsonProperty("label")
    private String label;

    @JsonProperty("prop")
    private String prop;

    @JsonProperty("verify")
    private String verify;

    @JsonProperty("type")
    private Map<String, String> type;

}
