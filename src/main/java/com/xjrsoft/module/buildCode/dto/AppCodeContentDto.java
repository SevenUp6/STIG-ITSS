package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppCodeContentDto {

    @JsonProperty("entityCode")
    private String entityCode;

    @JsonProperty("pageinputDtoCode")
    private String pageInputDtoCode;

    @JsonProperty("formoutputDtoCode")
    private String formOutputDtoCode;

    @JsonProperty("forminputDtoCode")
    private String formInputDtoCode;

    @JsonProperty("pageoutputDtoCode")
    private String pageOutputDtoCode;

    @JsonProperty("irepositoryCode")
    private String iRepositoryCode;

    @JsonProperty("repositoryCode")
    private String repositoryCode;

    @JsonProperty("iserviceCode")
    private String iServiceCode;

    @JsonProperty("serviceCode")
    private String serviceCode;

    @JsonProperty("controllerCode")
    private String controllerCode;

    @JsonProperty("listHTML")
    private String listHTML;

    @JsonProperty("formHTML")
    private String formHTML;
}
