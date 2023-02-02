package com.xjrsoft.common.dialect;

import com.xjrsoft.common.dbmodel.entity.TableInfo;

public class MysqlDialect extends Dialect {

    @Override
    public String forCreateTable(TableInfo tableInfo) {
        return null;
    }
}
