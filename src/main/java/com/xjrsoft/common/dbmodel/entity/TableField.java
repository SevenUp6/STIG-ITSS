package com.xjrsoft.common.dbmodel.entity;

import lombok.Data;

@Data
public class TableField {

    private String name;

    private String type;

    private String comment;

    private Integer length;

    private Boolean isKey;

    private Boolean isNullable;
}
