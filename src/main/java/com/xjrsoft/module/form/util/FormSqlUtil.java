package com.xjrsoft.module.form.util;

import com.xjrsoft.common.dbmodel.SqlParam;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;

import java.util.List;
import java.util.Map;

public class FormSqlUtil {
    private FormSqlUtil(){}

    public static SqlParam buildSimpleSelectFormSqlParam(String tableName, List<String> columns, Map<String, Object> conditionMap){
        SqlParam sqlParam = new SqlParam();
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        if (CollectionUtil.isNotEmpty(columns)) {
            boolean isFirst = true;
            for (String column : columns) {
                if (StringUtil.isBlank(column)) continue;
                if (isFirst) {
                    isFirst = false;
                } else {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append(column + " AS \"" + column + "\"");
            }
        }
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(tableName);
        if (CollectionUtil.isNotEmpty(conditionMap)) {
            sqlBuilder.append(" WHERE ");
            for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
                sqlBuilder.append(entry.getKey());
                sqlBuilder.append(" = ? ");
                sqlParam.addPara(entry.getValue());
            }
        }
        sqlParam.setSql(sqlBuilder.toString());
        return sqlParam;
    }

    public static SqlParam buildSimpleInsertSqlParam(String tableName, Map<String, Object> record) {
        SqlParam sqlParam = new SqlParam();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into ");
        sqlBuilder.append(tableName).append("(");
        StringBuilder temp = new StringBuilder();
        temp.append(") values(");
        boolean isFirst = true;
        for (Map.Entry<String, Object> e: record.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sqlBuilder.append(", ");
                temp.append(", ");
            }
            sqlBuilder.append(e.getKey());
            temp.append('?');
            sqlParam.addPara(e.getValue());
        }
        sqlBuilder.append(temp.toString()).append(')');
        sqlParam.setSql(sqlBuilder.toString());
        return sqlParam;
    }

    public static SqlParam buildSimpleUpdateSqlParam(String tableName, String pKey, Object id, Map<String, Object> record) {
        SqlParam sqlParam = new SqlParam();
        StringBuilder sqlBuilder = new StringBuilder();
        tableName = tableName.trim();
        sqlBuilder.append("update ").append(tableName).append(" set ");
        boolean isFirst = true;
        for (Map.Entry<String, Object> e: record.entrySet()) {
            String colName = e.getKey();
            if (!StringUtil.equalsIgnoreCase(colName, pKey)) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append(colName).append(" = ? ");
                sqlParam.addPara(e.getValue());
            }
        }
        sqlBuilder.append(" where ");
        sqlBuilder.append(pKey).append(" = ?");
        sqlParam.addPara(id);

        sqlParam.setSql(sqlBuilder.toString());
        return sqlParam;
    }

    public static SqlParam buildSimpleDeleteSqlParam(String tableName, String field, Object...values) {
        SqlParam sqlParam = new SqlParam();
        tableName = tableName.trim();

        StringBuilder sqlBuilder = new StringBuilder("delete from ").append(tableName).append(" where ");
        sqlBuilder.append(field);
        if (values != null && values.length == 1) {
            sqlBuilder.append(" = ?");
            sqlParam.addPara(values[0]);
        } else {
            sqlBuilder.append(" in(");
            for (int i = 0; i < values.length; i++) {
                sqlParam.addPara(values[i]);
                if (i != 0) {
                    sqlBuilder.append(StringPool.COMMA);
                }
                sqlBuilder.append(StringPool.QUESTION_MARK);
            }
            sqlBuilder.append(StringPool.RIGHT_BRACKET);
        }
        sqlParam.setSql(sqlBuilder.toString());
        return sqlParam;
    }

    public static String forPaginate(int pageNumber, int pageSize, String sql) {
        StringBuilder findSql = new StringBuilder(sql);
        int offset = pageSize * (pageNumber - 1);
        findSql.append(" limit ").append(offset).append(", ").append(pageSize);	// limit can use one or two '?' to pass paras
        return findSql.toString();
    }
}
