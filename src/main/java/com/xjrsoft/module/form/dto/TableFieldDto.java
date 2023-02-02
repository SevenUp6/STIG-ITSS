package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableFieldDto {

    @JsonProperty("fieldName")
    private String name;

    @JsonProperty("dataType")
    private String type;

    @JsonProperty("describe")
    private String comment;

    @JsonProperty("fieldLength")
    private Integer length;
}
