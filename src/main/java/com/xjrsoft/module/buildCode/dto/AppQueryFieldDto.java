package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppQueryFieldDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("field")
    private String field;

    @JsonProperty("type")
    private String type;

}
