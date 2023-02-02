package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.Data;

@Data
public class DbTableDto {

    private String name;

    private String pk;

    private Integer parentName;

    private String field;

    private String relationName;

    private String relationField;

    @JsonIgnore
    private Boolean isMainTable;

    public boolean isMainTable(){
        if (isMainTable == null) {
            if (StringUtil.isNoneBlank(relationField, relationName)) {
                isMainTable = false;
            } else {
                isMainTable = true;
            }
        }
        return isMainTable;
    }
}
