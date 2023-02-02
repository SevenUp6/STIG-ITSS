package com.xjrsoft.module.form.dto;

import com.xjrsoft.common.page.PageInput;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormSchemePageListDto extends PageInput {

    @ApiModelProperty(value = "分类")
    private String category;
}
