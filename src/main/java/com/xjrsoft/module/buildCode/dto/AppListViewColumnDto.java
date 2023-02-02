package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AppListViewColumnDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("datatype")
    private String datatype;

    @JsonProperty("dfvalue")
    private String defaultValue;

    @JsonProperty("field")
    private String field;

    @JsonProperty("isHide")
    private String isHide;

    @JsonProperty("table")
    private String table;

    @JsonProperty("select")
    private String select;

    @JsonProperty("type")
    private String type;

    @JsonProperty("listStyle")
    private Map<String, Object> listStyle;
}
