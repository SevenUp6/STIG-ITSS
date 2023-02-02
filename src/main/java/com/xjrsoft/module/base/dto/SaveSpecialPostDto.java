package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SaveSpecialPostDto {

    @JsonProperty("F_Type")
    private Integer type;

    @JsonProperty("postList")
    private List<SpecialPostDto> specialPostDtoList;
}
