package com.xjrsoft.common.dbmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据库表信息
 */
@Data
@AllArgsConstructor
public class DbTableInfo implements Serializable {

    /**
     * 表名
     */
    @JsonProperty("Table_Name")
    private String tableName;

    /**
     * 行
     */
    @JsonProperty("Table_Rows")
    private String tableRows;

    /**
     * 主键
     */
    @JsonProperty("Table_PK")
    private String tablePK;

    /**
     * 索引大小
     */
    @JsonProperty("Index_Size")
    private String indexSize;

    /**
     * 数据量
     */
    @JsonProperty("Data_Size")
    private String dataSize;

    /**
     *  备注
     */
    @JsonProperty("Description")
    private String description;

}
