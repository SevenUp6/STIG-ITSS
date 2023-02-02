package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class BaseInfoDto {

    private String createUser;

    private String describe;

    private String name;

    /**
     * "F_ItemName": ""
     * "F_ItemValue": "companyManage"
     */
    private Map<String, String> outputArea;

    private String fontDirectory;

    private String controllerOutPutDir;

    private String modelOutputDir;

    private String formSize;

    private Boolean haveSql;

    @JsonProperty("onlyFont")
    private Boolean isOnlyFont;

    private Boolean isWorkflowForm;
}
