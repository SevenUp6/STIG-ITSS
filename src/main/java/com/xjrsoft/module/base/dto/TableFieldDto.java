package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableFieldDto {

    @JsonProperty("f_column")
    private String name;

    @JsonProperty("f_datatype")
    private String type;

    @JsonProperty("f_remark")
    private String comment;

    @JsonProperty("f_length")
    private Integer length;

    @JsonProperty("f_key")
    private Boolean isKey;

    @JsonProperty("f_isnullable")
    private Boolean isNullable;
}
