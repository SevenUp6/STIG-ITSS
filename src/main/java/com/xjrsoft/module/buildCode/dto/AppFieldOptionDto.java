package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AppFieldOptionDto {

    @JsonProperty("column")
    private List<AppSubFromFieldDto> columnList;
}
