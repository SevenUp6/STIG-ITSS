package com.xjrsoft.common.dbmodel.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.xjrsoft.common.dbmodel.entity.TableField;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {

    private SqlUtil() {
    }

    public static List<String> buildCreateTableSql(DbType dbType, List<TableInfo> tableInfoList) {
        List<String> sqlList = new ArrayList<>();
        for (TableInfo tableInfo : tableInfoList) {
            sqlList.addAll(buildCreateTableSql(dbType, tableInfo));
        }
        return sqlList;
    }

    public static List<String> buildCreateTableSql(DbType dbType, TableInfo tableInfo) {
        String sql = StringPool.EMPTY;
        List<String> sqlList = new ArrayList<>();
        if (dbType == DbType.MYSQL) {
            sql = getMysqlCreateTableSql(tableInfo);
        } else if (dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C) {
            sql = getOracleCreateTableSql(tableInfo);
        } else if (dbType == DbType.SQL_SERVER || dbType == DbType.SQL_SERVER2005){
            sqlList.addAll(getSqlServerCreateTableSql(tableInfo));
        }
        if (!StringUtil.isEmpty(sql)) {
            sqlList.add(sql);
        }
        if (DbType.ORACLE == dbType) {
            List<String> oracleTableCommentSqls = getOracleTableCommentSqls(tableInfo);
            oracleTableCommentSqls.stream().forEach(oracleSql -> sqlList.add(oracleSql));
        }
        return sqlList;
    }

    /**
     * @return 建表SQL语句
     * @author 光华科技-软件研发部
     * @date：2019年10月14日 @description：构建sqlserver数据库创建表sql
     */
    private static List<String> getSqlServerCreateTableSql(TableInfo tableInfo) {
        List<String> resultSqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();
        createSql.append("CREATE TABLE " + tableInfo.getName() + " (");
        for (TableField field : tableInfo.getFields()) {
            String dataType = field.getType();
            createSql.append(field.getName() + " " + dataType);
            Integer length = field.getLength();
            if (StringUtils.equalsIgnoreCase(dataType, "varchar") && length != null && !Integer.valueOf(0).equals(length)) {
                createSql.append("(" + length + ")");
            }
            if (BooleanUtils.isTrue(field.getIsKey())) {
                createSql.append(" PRIMARY KEY ");
            } else if (BooleanUtils.isTrue(field.getIsNullable())) {
                createSql.append(" NOT NULL ");
            }
            createSql.append(",");
        }
        createSql.delete(createSql.length() - 1, createSql.length());
        createSql.append(");");
        resultSqlList.add(createSql.toString());

        for (TableField field : tableInfo.getFields()) {
            String remark = field.getComment();
            if (StringUtil.isNotBlank(remark)) {
                // 添加列备注
                resultSqlList.add(" execute sp_addextendedproperty 'MS_Description','" + remark + "','user','dbo','table','"
                        + tableInfo.getName() + "','column','" + field.getName() + "';");
            }
        }

        // 添加表备注
        if (StringUtil.isNotBlank(tableInfo.getComment())) {
            resultSqlList.add(" execute sp_addextendedproperty 'MS_Description','" + tableInfo.getComment() + "','user','dbo','table','"
                    + tableInfo.getName() + "',null,null;  ");
        }
        return resultSqlList;
    }

    /**
     * @return 建表SQL语句
     * @author 光华科技-软件研发部
     * @date：2019年10月14日 @description：构建mysql数据库创建表sql
     */
    private static String getMysqlCreateTableSql(TableInfo tableInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + tableInfo.getName() + " ( ");// 表名
        for (TableField field : tableInfo.getFields()) {
            String dataType = field.getType();
            Integer length = field.getLength();
            sql.append(field.getName() + " " + dataType);// 列名+类型
            if (StringUtils.equalsIgnoreCase(dataType, "varchar")) {
                if (length == null || Integer.valueOf(0).equals(length)) {
                    length = 100;
                }
                sql.append("(" + length + ") ");// 长度
            }
            if (BooleanUtils.isTrue(field.getIsKey())) {
                sql.append(" PRIMARY KEY ");// 是否主键
            } else if (BooleanUtils.isTrue(field.getIsNullable())) {
                sql.append(" NOT NULL ");// 是否为空
            }
            String remark = field.getComment();
            if (StringUtil.isNotBlank(remark)) {
                sql.append(" COMMENT '" + remark + "'");// 列备注
            }
            sql.append(",");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" )");
        // 添加表备注
        if (StringUtil.isBlank(tableInfo.getComment())) {
            sql.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" + tableInfo.getComment() + "';");
        }

        return sql.toString();
    }

    private static String getOracleCreateTableSql(TableInfo tableInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + tableInfo.getName().toUpperCase() + " ( ");// 表名
        for (TableField field : tableInfo.getFields()) {
            sql.append(field.getName().toUpperCase() + " "
                    + getOracleDataType(field.getType()));// 列名+类型
            if (StringUtils.equals(field.getType(), "varchar")) {
                Integer length = field.getLength();
                if (length == null || Integer.valueOf(0).equals(length)) {
                    length = 100;
                }
                sql.append("(" + length + " CHAR) ");// 长度
            }
            if (BooleanUtils.isTrue(field.getIsKey())) {
                sql.append(" PRIMARY KEY NOT NULL ");// 是否主键
            } else if (BooleanUtils.isTrue(field.getIsNullable())) {
                sql.append(" NOT NULL ");// 是否为空
            } else {
                sql.append(" NULL ");// 是否为空
            }
            sql.append(",");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" )");
        return sql.toString();
    }

    public static List<String> getOracleTableCommentSqls(TableInfo tableInfo) {
        List<String> sqls = new ArrayList<String>();
        //// 添加表备注
        if (StringUtil.isNotBlank(tableInfo.getComment())) {
            sqls.add(" COMMENT ON TABLE " + StringUtils.upperCase(tableInfo.getName()) + " is '" + tableInfo.getComment() + "'  ");
        }
        for (TableField field : tableInfo.getFields()) {
            String remark = field.getComment();
            if (StringUtil.isNotBlank(remark)) {
                // 添加列备注
                sqls.add(" COMMENT ON COLUMN " + tableInfo.getName().toUpperCase() + "."
                        + StringUtils.upperCase(field.getName()) + " is '" + remark + "'");
            }
        }
        return sqls;
    }

    private static String getOracleDataType(String dataType) {
        String res = "";
        switch (dataType) {
            case "varchar":
                res = "VARCHAR2";
                break;
            case "datetime":
                res = "DATE";
                break;
            case "int":
                res = "INTEGER";
                break;
            case "decimal":
                res = "NUMBER(11)";
                break;
        }
        return res;
    }
}
