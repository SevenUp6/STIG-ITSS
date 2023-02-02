package com.xjrsoft.common.dialect;

import com.xjrsoft.common.dbmodel.entity.TableInfo;

public abstract class Dialect {

    /**
     * 生成建表语句
     * @param tableInfo
     * @return
     */
    public abstract String forCreateTable(TableInfo tableInfo);
}
