package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TableInfoDto {

    @JsonProperty("tableName")
    private String name;

    @JsonProperty("tableDescription")
    private String comment;

    @JsonProperty("columns")
    private List<TableFieldDto> fields;
}
