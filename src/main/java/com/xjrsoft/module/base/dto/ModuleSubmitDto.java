package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModuleSubmitDto {

    @JsonProperty("module")
    private ModuleDto moduleDto;

    @JsonProperty("buttonModule")
    private List<ModuleButtonDto> moduleButtonDtoList;

    @JsonProperty("columnModule")
    private List<ModuleColumnDto> moduleColumnDtoList;

    @JsonProperty("formModule")
    private List<ModuleFormDto> moduleFormDtoList;
}
