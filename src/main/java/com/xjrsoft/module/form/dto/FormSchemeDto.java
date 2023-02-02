package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FormSchemeDto {

    @JsonProperty("scheme")
    private SchemeDto schemeDto;

    @JsonProperty("schemeInfo")
    private SchemeInfoDto schemeInfoDto;

    @JsonProperty("tableInfo")
    private List<TableInfoDto> tableInfoDtoList;
}
