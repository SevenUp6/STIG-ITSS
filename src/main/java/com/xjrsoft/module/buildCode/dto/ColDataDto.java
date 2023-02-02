package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ColDataDto {

    private String isPage;

    private Integer isTree;

    @JsonProperty("btns")
    private List<Map<String, Object>> buttonList;

    @JsonProperty("fields")
    private List<Map<String, Object>> columnList;

    @JsonProperty("treeform")
    private Map<String, Object> treeForm;

    private  Integer pageSize;

    @JsonProperty("impData")
    private ImportDataDto importDataDto;
}
