package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DbSettingDto {

    @JsonProperty("DbId")
    private String dbId;

    @JsonProperty("list")
    List<DbTableDto> dbTableDtoList;
}
