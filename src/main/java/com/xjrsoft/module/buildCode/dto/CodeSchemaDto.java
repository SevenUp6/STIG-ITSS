package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class CodeSchemaDto {

    @JsonProperty("dbLinkId")
    private String dbLinkId;

    @JsonProperty("dbTable")
    private List<DbTableDto> dbTableDtoList;

    @JsonProperty("baseInfo")
    private BaseInfoDto baseInfoDto;

    @JsonProperty("colData")
    private ColDataDto colDataDto;

    @JsonProperty("formData")
    private List<FormDataDto> formDataDtoList;

    @JsonProperty("queryData")
    private List<QueryDataDto> queryDataDtoList;

    @JsonProperty("moduleData")
    private ModuleDataDto moduleDataDto;

    @JsonProperty("totalData")
    private List<TotalDataDto> totalDataDto;

    @JsonProperty("codeContent")
    private CodeContentDto codeContentDto;

    @JsonProperty("tableInfo")
    private List<TableInfoDto> tableInfoDtoList;

}
