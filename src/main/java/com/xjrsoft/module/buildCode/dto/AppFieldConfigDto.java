package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppFieldConfigDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("datatype")
    private String dataType;

    @JsonProperty("dataSource")
    private String dataSource;

    @JsonProperty("dataItem")
    private String dataItem;

    @JsonProperty("showfield")
    private String showField;

    @JsonProperty("savefield")
    private String saveField;

    @JsonProperty("type")
    private String type;

    @JsonProperty("table")
    private String table;

    @JsonProperty("field")
    private String field;

    @JsonProperty("dbdefault")
    private Object defaultValue;

    @JsonProperty("verify")
    private String verify;

    @JsonProperty("info")
    private String info;
    @JsonProperty("imgLength")
    private Integer imgLength;

    @JsonProperty("dateformat")
    private String dateFormat;

    @JsonProperty("option")
    private AppFieldOptionDto optionDto;
}
