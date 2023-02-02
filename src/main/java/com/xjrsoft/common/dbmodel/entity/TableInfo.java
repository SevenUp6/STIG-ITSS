package com.xjrsoft.common.dbmodel.entity;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {

    private String name;

    private String comment;

    private List<TableField> fields;
}
