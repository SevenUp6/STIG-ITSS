package com.xjrsoft.module.excel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.page.PageInput;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "ExcelImportDto", description = "excel导入分页入参")
public class GetListExcelImportDto extends PageInput {

    @ApiModelProperty(value = "模板Id")
    @JsonProperty("F_ModuleId")
    private String F_ModuleId;
}
