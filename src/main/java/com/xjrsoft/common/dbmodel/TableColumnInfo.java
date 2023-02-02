package com.xjrsoft.common.dbmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TableColumnInfo implements Serializable {
    /**
     * 列Id
     */
    @JsonProperty("ColumnId")
    private String columnId;

    /**
     * 列名
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 类型
     */
    @JsonProperty("Type")
    private String type;

    /**
     * 是否为主键（1是 | 0否）
     */
    @JsonProperty("IsKey")
    private Integer isKey;

    /**
     * 默认值
     */
    @JsonProperty("DefaultValue")
    private String defaultValue;

    /**
     * 长度
     */
    @JsonProperty("Length")
    private Long Length;

    /**
     * 是否可空（1是 | 0否）
     */
    @JsonProperty("IsNullable")
    private Integer isNullable;

    /**
     *  备注
     */
    @JsonProperty("Description")
    private String description;
}
