package com.xjrsoft.module.excel.dto;

import com.xjrsoft.common.page.PageInput;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetListExcelExportDto extends PageInput {

    @ApiModelProperty(value = "模板Id")
    private String F_ModuleId;
}
