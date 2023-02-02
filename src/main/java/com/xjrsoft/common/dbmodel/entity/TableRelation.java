package com.xjrsoft.common.dbmodel.entity;

import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.Data;

@Data
public class TableRelation {

    /**
     * 表名
     */
    private String name;

    /**
     * 主键值
     */
    private String pk;

    /**
     *
     */
    private String parentName;

    /**
     * 用于关联的字段
     */
    private String field;

    /**
     * 关联表表名
     */
    private String relationName;

    /**
     * 用于关联表的字段
     */
    private String relationField;


    /**
     * 判断是不是主表，没有关联表即为主表
     * @return
     */
    public boolean isMainTable() {
        return StringUtil.isEmpty(this.relationName);
    }
}
