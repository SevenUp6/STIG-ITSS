package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TableInfoDto {
    @JsonProperty("formName")
    private String name;

    @JsonProperty("dataChildren")
    private List<TableFieldDto> fields;
}
