package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ReleaseTableDto {

    @JsonProperty("id")
    private String dbLinkId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("remark")
    private String comment;

    @JsonProperty("content")
    private List<TableFieldDto> fields;
}
