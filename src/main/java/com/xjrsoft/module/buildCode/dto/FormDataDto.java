package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class FormDataDto {

    @JsonProperty("__config__")
    private Map<String, Object> config;

    @JsonProperty("__type__")
    private String componentType;

    @JsonProperty("infoType")
    private String infoType;

    @JsonProperty("__organize__")
    private Boolean organize;

    @JsonProperty("placeholder")
    private String placeholder;

    @JsonProperty("style")
    private Map<String, Object> style;

    @JsonProperty("__info__")
    private Boolean info;

    @JsonProperty("disabled")
    private Boolean disabled;

    @JsonProperty("readonly")
    private Boolean readonly;

    @JsonProperty("__vModel__")
    private String __vModel__;

    @JsonProperty("__slot__")
    private Map<String, Object> slot;
}
