package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ImportDataDto {

    private String moduleName;

    private String bindBtn;
    private String errorRule;
    @JsonProperty("impField")
    private List<ImportFieldsDto> importFieldsDtoList;
}
