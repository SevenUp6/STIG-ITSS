package com.xjrsoft.module.excel.dto;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@ApiModel(value = "BaseExcelImportDto", description = "新增/更新excelImport导入dto")
public class BaseExcelImportDto {

    private static final long serialVersionUID = 1L;


    @JsonProperty("import")
    private ExcelImportDto excelImport;


    @JsonProperty("importFieldList")
    private List<ExcelImportFieldsDto> importFieldList;

}
