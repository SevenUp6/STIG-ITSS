package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableFieldDto {

    @JsonProperty("fieldName")
    private String name;

    @JsonProperty("fieldType")
    private String type;

    @JsonProperty("fieldLength")
    private Integer length;

    @JsonProperty("description")
    private String comment;
}
