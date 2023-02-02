package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FormRelationSavedDto {

    @JsonProperty("formRelation")
    private FormRelationDto formRelation;

    @JsonProperty("module")
    private ModuleFormDto module;
}
