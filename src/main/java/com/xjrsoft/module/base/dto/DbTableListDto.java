package com.xjrsoft.module.base.dto;

import com.xjrsoft.common.page.PageInput;
import lombok.Data;

@Data
public class DbTableListDto extends PageInput {

    private String tableName;
}
