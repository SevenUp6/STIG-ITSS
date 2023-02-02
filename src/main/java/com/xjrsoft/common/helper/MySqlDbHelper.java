package com.xjrsoft.common.helper;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.core.tool.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;

/**
 * @title mysql数据库帮助类
 * @desc  集成父类，
 * @author tzx
 * @create 2020年11月7日 14:27:19
 * */
public class MySqlDbHelper extends DbHelper {

    /**
     * @param dbType   数据库类型   mybatis-plus 枚举类型
     * @param conn     连接字符串
     * @param userName 用户名
     * @param passWord 密码
     * @title 构造函数
     */
    public MySqlDbHelper(DbType dbType, String conn, String userName, String passWord,String dbName) throws SQLException, ClassNotFoundException {
        super(dbType, conn, userName, passWord,dbName);
    }

    public MySqlDbHelper(DbType dbType, String dbLinkId) throws SQLException, ClassNotFoundException {
        super(dbType, dbLinkId);
    }

    @Override
    public List<DbTableInfo> getDbTableInfo(String tableName, String schemeName, IPage<DbTableInfo> page) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.table_schema AS 'DB_Name',");
        sql.append(" t.table_name AS 'Table_Name',");
        sql.append(" t.table_rows AS 'Table_Rows',");
        sql.append(" MIN(k.column_name)  AS 'Table_PK',");
        sql.append(" TRUNCATE(t.data_length / 1024 , 2) AS 'Data_Size',");
        sql.append(" TRUNCATE(t.index_length / 1024 , 2) AS 'Index_Size',");
        sql.append(" t.table_comment AS 'Description'");
        sql.append(" FROM information_schema.TABLES t");
        sql.append(" JOIN information_schema.key_column_usage k USING (table_schema,table_name)");
        sql.append(" WHERE ");
        if (StringUtils.isNotBlank(tableName))
        {
            sql.append(" table_name like ? AND ");
        }
        sql.append("  table_schema = '").append(super.getDbName()).append("'   ");
        sql.append("  GROUP BY ");
        sql.append("   t.table_schema, ");
        sql.append("   t.table_name, ");
        sql.append("   t.table_rows, ");
        sql.append("   t.table_comment, ");
        sql.append("   TRUNCATE (t.data_length / 1024, 2), ");
        sql.append("   TRUNCATE(t.index_length / 1024 , 2) ");


        List<DbTableInfo> dbInfoList = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(tableName)) {
            params.add(tableName);
        }
        String runSql = sql.toString();
        if (page != null) {
            // 分页
            // 查询数据总数
            Integer total = count(sql.toString(), params);
            page.setTotal(total);
            if (total != null && total > 0) {
                runSql = this.wrapPageSql(sql, page).toString();
            }
        }
        PreparedStatement preparedStatement = super.getConnection().prepareStatement(runSql);

        this.setParameters(preparedStatement, params);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            DbTableInfo model = new DbTableInfo(
                    resultSet.getString("Table_Name"),
                    resultSet.getString("Table_Rows"),
                    resultSet.getString("Table_PK"),
                    resultSet.getString("Index_Size"),
                    resultSet.getString("Data_Size"),
                    resultSet.getString("Description"));
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
            if(sql.toLowerCase().contains(filterCode)){
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
            if( sql.toLowerCase().contains(filterCode) ){
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
        sql.append("select DISTINCT  a.COLUMN_NAME as Name, ");
        sql.append(" a.DATA_TYPE as Type,(a.COLUMN_KEY = 'PRI') as IsKey, ");
        sql.append(" (a.IS_NULLABLE = 'YES') as IsNullable, a.CHARACTER_MAXIMUM_LENGTH as Length, ");
        sql.append("  a.COLUMN_DEFAULT as DefaultValue,a.COLUMN_COMMENT as Description, a.ORDINAL_POSITION");
        sql.append("  from information_schema.columns a ");

        sql.append(" where ");
        if (!tableName.isEmpty())
        {
            sql.append("table_name = ? AND");
        }
        sql.append("  table_schema = '").append(super.getDbName()).append("'  ORDER BY a.ORDINAL_POSITION");


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
                    resultSet.getString("Name"),
                    resultSet.getString("Type"),
                    resultSet.getInt("IsKey"),
                    resultSet.getString("DefaultValue"),
                    resultSet.getLong("Length"),
                    resultSet.getInt("IsNullable"),
                    resultSet.getString("Description"));
            columnInfos.add(model);
        }
        super.getConnection().close();
        preparedStatement.close();
        return  columnInfos;
    }

    @Override
    public List<Map<String, Object>> getDataByIdAndTableName(String tableName) throws SQLException {

        String sql = "SELECT * FROM `" + tableName + "` limit 100" ;

        PreparedStatement preparedStatement = super.getConnection().prepareStatement(sql);

//        if (StringUtils.isNotBlank(tableName))
//        {
//            preparedStatement.setObject(1,  tableName );
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
        Statement statement = super.getStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();

        while (resultSet.next()){
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnLabel(i);
                Object object = resultSet.getObject(i);
                rowData.put(columnName, object);
            }
            list.add(rowData);
        }
        super.getConnection().close();
        statement.close();
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
            super.getStatement().execute("SELECT 1");
            super.getConnection().close();
            super.getStatement().close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getCheckTableNamesSql(Integer nameNum) {
        StringBuilder sql = new StringBuilder("select `TABLE_NAME` as \"tableName\" from information_schema.`TABLES` where `TABLE_NAME` in (");
        for (int i = 0; i < nameNum; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") and `TABLE_SCHEMA` = '" + this.getDbName() + "'");
        return sql.toString();
    }

    protected StringBuilder wrapPageSql(StringBuilder find, IPage page) {
        return find.append(" LIMIT ").append(PageUtil.getStart((int) page.getCurrent(), (int) page.getSize())).append(", ").append(page.getSize());
    }
}
