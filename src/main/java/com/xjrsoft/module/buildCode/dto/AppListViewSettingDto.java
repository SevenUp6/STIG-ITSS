package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AppListViewSettingDto {

    @JsonProperty("list")
    private List<AppListViewColumnDto> listViewColumnDtoList;

    @JsonProperty("btn")
    private List<AppColumnBtnDto> columnBtnDtoList;
}
