package com.xjrsoft.common.helper;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.xjrsoft.common.core.SplicingConnectionString;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.core.tool.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title 达梦数据库帮助类
 * @desc  集成父类，
 * @author tzx
 * @create 2021年3月11日 18:00:46
 * */
public class DmDbHelper extends DbHelper {

    /**
     * @param dbType   数据库类型   mybatis-plus 枚举类型
     * @param conn     连接字符串
     * @param userName 用户名
     * @param passWord 密码
     * @title 构造函数
     */
    public DmDbHelper(DbType dbType, String conn, String userName, String passWord, String dbName) throws SQLException, ClassNotFoundException {
        super(dbType, conn, userName, passWord,dbName);
    }

    @Override
    public List<DbTableInfo> getDbTableInfo(String tableName, String schemeName, IPage<DbTableInfo> page) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" t1.table_name TABLE_NAME, ");
        sql.append(" t1.num_rows Table_Rows, ");
        sql.append(" t2.COMMENTS Description, ");
        sql.append(" listagg (t3.COLUMN_NAME, ',') WITHIN GROUP (ORDER BY t3.COLUMN_NAMe) Table_PK, ");
        sql.append(" '未知' Index_Size, ");
        sql.append(" '未知' Data_Size ");
        sql.append(" FROM ");
        sql.append(" user_tables t1 ");
        sql.append(" LEFT JOIN user_tab_comments t2 ON t2.table_name = t1.table_name ");
        sql.append(" LEFT JOIN user_cons_columns t3 ON t3.table_name = t2.table_name ");
        sql.append(" LEFT JOIN user_constraints t4 ON t4.constraint_name = t3.constraint_name ");
        sql.append(" WHERE ");
        sql.append(" t4.constraint_type = 'P' AND t1.table_name not like '%ACT%' ");
        if (StringUtils.isNotBlank(tableName))
        {
            sql.append(" AND t1.table_name =  ? ");
        }
        sql.append(" GROUP BY ");
        sql.append(" t1.table_name, ");
        sql.append(" t1.num_rows, ");
        sql.append(" t2.COMMENTS");

        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(tableName)) {
            params.add(tableName);
        }
        String runSql = sql.toString();
        if (page != null) {
            Integer total = count(sql.toString(), params);
            page.setTotal(total);
            if (total != null && total > 0) {
                int start = PageUtil.getStart((int) page.getCurrent(), (int) page.getSize());
                runSql = "select * from (" + sql.toString() + ") t LIMIT " + start + "," + page.getSize();
            }
        }
        List<DbTableInfo> dbInfoList = new ArrayList<>();
        PreparedStatement preparedStatement = super.getConnection().prepareStatement(runSql);

        this.setParameters(preparedStatement, params);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            DbTableInfo model = new DbTableInfo(
                    resultSet.getString("TABLE_NAME"),
                    resultSet.getString("TABLE_ROWS"),
                    resultSet.getString("TABLE_PK"),
                    resultSet.getString("INDEX_SIZE"),
                    resultSet.getString("DATA_SIZE"),
                    resultSet.getString("DESCRIPTION"));
            dbInfoList.add(model);
//                DbTableInfo info = BeanUtil.copy(resultSet.getObject(i), DbTableInfo.class);
//                dbInfoList.add(info);
        }
        preparedStatement.close();
        super.getConnection().close();
        return  dbInfoList;
    }

    @Override
    public List<Map<String,Object>> getTableData(String sql, String schemeName) throws ClassNotFoundException, SQLException {

        // 已经替换掉字符的前提下，关键要生效就必须后面带最少一个半角空格
        for(String filterCode:filterList){
            if( sql.toLowerCase().contains(filterCode) ){
                throw new SQLException("此sql包含敏感词汇,请修改!");
            }
        }
//        Class.forName(SplicingConnectionString.getDriverString(super.getDbType()));
        Connection connection = super.getConnection();
        Statement statement = connection.createStatement();


        ResultSet resultSet = statement.executeQuery(sql);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();

        while (resultSet.next()){
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnName(i);
                Object object = resultSet.getObject(i);
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        statement.close();
        connection.close();
        return list;
    }

    @Override
    public List<String> getTableField(String sql, String schemeName) throws SQLException {

        // 已经替换掉字符的前提下，关键要生效就必须后面带最少一个半角空格
        for(String filterCode:filterList){
            if( sql.toLowerCase().contains(filterCode)){
                throw new SQLException("此sql包含敏感词汇,请修改!");
            }
        }
        List<String> list = new ArrayList<String>();
        try {
            ResultSet resultSet = super.getStatement().executeQuery(sql);
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                list.add(md.getColumnName(i));
            }
        } catch (SQLSyntaxErrorException | SQLServerException e) {
            throw new SQLException("SQL语句错误，请输入正确的SQL语句!", e);
        }
        super.getConnection().close();
        super.getStatement().close();
        return list;
    }

    @Override
    public List<TableColumnInfo> getTableFieldByName(String tableName) throws ClassNotFoundException, SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" col.column_id num, ");
        sql.append(" col.column_name name, ");
        sql.append(" CASE WHEN (col.data_type = 'NUMBER' AND NVL (col.DATA_SCALE, 0) <> 0) THEN(col.data_type || '(' || col.data_precision || ',' || col.data_scale || ')') ELSE col.data_type END type,");
        sql.append(" CASE WHEN ( col.data_type = 'NUMBER' OR COL.data_type = 'Varchar2' ) THEN col.data_length / 2 ELSE col.data_length END length, ");
        sql.append(" NULL identity, ");
        sql.append(" CASE uc.constraint_type WHEN 'P' THEN 1 ELSE NULL END iskey, ");
        sql.append(" CASE col.nullable WHEN 'N' THEN 0 ELSE 1 END isnullable, ");
        sql.append(" col.data_default defaultvalue, ");
        sql.append(" NVL ( comm.comments, col.column_name ) AS Description ");
        sql.append(" FROM user_tab_columns col ");
        sql.append(" INNER JOIN user_col_comments comm ON comm.TABLE_NAME = col.TABLE_NAME ");
        sql.append(" AND comm.COLUMN_NAME = col.COLUMN_NAME ");
        sql.append(" LEFT JOIN user_cons_columns ucc ON ucc.table_name = col.table_name ");
        sql.append(" AND ucc.column_name = col.column_name ");
        sql.append(" AND ucc.position = 1 ");
        sql.append(" LEFT JOIN user_constraints uc ON uc.constraint_name = ucc.constraint_name ");
        sql.append(" AND uc.constraint_type = 'P' ");
        if (!tableName.isEmpty())
        {
            sql.append(" WHERE   ");
            sql.append(" col.table_name =  ?'");
        }
        sql.append(" ORDER BY col.column_id");


        PreparedStatement preparedStatement = super.getConnection().prepareStatement(sql.toString());

        if (StringUtils.isNotBlank(tableName))
        {
            preparedStatement.setObject(1,  tableName);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<TableColumnInfo> columnInfos = new ArrayList<>();
        int i = 0;
        while (resultSet.next()){
            i++;
            TableColumnInfo model = new TableColumnInfo(
                    Integer.toString(i),
                    resultSet.getString("NAME"),
                    resultSet.getString("TYPE"),
                    resultSet.getInt("ISKEY"),
                    resultSet.getString("DEFAULTVALUE"),
                    resultSet.getLong("LENGTH"),
                    resultSet.getInt("ISNULLABLE"),
                    resultSet.getString("DESCRIPTION"));
            columnInfos.add(model);
        }
        super.getConnection().close();
        preparedStatement.close();
        return  columnInfos;
    }

    @Override
    public List<Map<String, Object>> getDataByIdAndTableName(String tableName) throws SQLException {

        //用语句包一层 可以保证sql 只查空数据  可以得到列名就行 不需要数据
        String sql = "SELECT * FROM " + tableName + " LIMIT 100";

        PreparedStatement preparedStatement = super.getConnection().prepareStatement(sql);

//        if (StringUtils.isNotBlank(tableName))
//        {
//            preparedStatement.setObject(0,  tableName );
//        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();

        while (resultSet.next()){
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnName(i);
                Object object = resultSet.getObject(i);
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        super.getConnection().close();
        preparedStatement.close();
        return list;
    }

    @Override
    public List<Map<String, Object>> getDataMapBySql(String sql) throws SQLException {

        // 已经替换掉字符的前提下，关键要生效就必须后面带最少一个半角空格
        for(String filterCode:filterList){
            if( sql.toLowerCase().contains(filterCode) ){
                throw new SQLException("此sql包含敏感词汇,请修改!");
            }
        }
        ResultSet resultSet = super.getStatement().executeQuery(sql);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();

        while (resultSet.next()){
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnName(i);
                Object object = resultSet.getObject(i);
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        super.getConnection().close();
        super.getStatement().close();
        return list;
    }

    @Override
    public List<Map<String, Object>> getDataMapBySqlAndColumns(String sql, String columns) throws SQLException {

        // 已经替换掉字符的前提下，关键要生效就必须后面带最少一个半角空格
        for(String filterCode:filterList){
            if( sql.toLowerCase().contains(filterCode) ){
                throw new SQLException("此sql包含敏感词汇,请修改!");
            }
        }
        ResultSet resultSet = super.getStatement().executeQuery("SELECT " + StringUtil.trimEnd(columns,",")  + " FROM (" + sql + ") t");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();

        while (resultSet.next()){
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnName(i);
                Object object = resultSet.getObject(i);
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        super.getConnection().close();
        super.getStatement().close();
        return list;
    }

    @Override
    public boolean testConnection(){
        try {
            super.getStatement().execute("SELECT 1 FROM DUAL");
            super.getConnection().close();
            super.getStatement().close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getCheckTableNamesSql(Integer nameNum) {
        return "";
    }
}