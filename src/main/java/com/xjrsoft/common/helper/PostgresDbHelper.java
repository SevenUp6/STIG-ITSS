package com.xjrsoft.common.helper;

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
 * @title postgres数据库帮助类
 * @desc  集成父类，
 * @author tzx
 * @create 2021年3月15日 10:52:59
 * */
public class PostgresDbHelper extends DbHelper {

    /**
     * @param dbType   数据库类型   mybatis-plus 枚举类型
     * @param conn     连接字符串
     * @param userName 用户名
     * @param passWord 密码
     * @title 构造函数
     */
    public PostgresDbHelper(DbType dbType, String conn, String userName, String passWord, String dbName) throws SQLException, ClassNotFoundException {
        super(dbType, conn, userName, passWord,dbName);
    }

    public PostgresDbHelper(DbType dbType, String dbLinkId) throws SQLException, ClassNotFoundException {
        super(dbType, dbLinkId);
    }

    @Override
    public List<DbTableInfo> getDbTableInfo(String tableName, String schemeName, IPage<DbTableInfo> page) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append(" select pg_tables.tablename AS TABLE_NAME, pg_constraint.conname as pk_name,pg_attribute.attname as TABLE_PK,pg_type.typname as typename, ");
        sql.append(" obj_description(relfilenode,'pg_class') as DESCRIPTION,pg_class.reltuples as TABLE_ROWS, '未知' as INDEX_SIZE, '未知'  as DATA_SIZE from pg_constraint  ");
        sql.append(" inner join pg_class on pg_constraint.conrelid = pg_class.oid  ");
        sql.append(" inner join pg_attribute on pg_attribute.attrelid = pg_class.oid and  pg_attribute.attnum = pg_constraint.conkey[1] ");
        sql.append(" inner join pg_type on pg_type.oid = pg_attribute.atttypid ");
        sql.append(" inner join pg_tables on pg_class.relname = pg_tables.tablename ");
        sql.append(" where pg_constraint.contype='p' and pg_tables.tablename NOT LIKE 'pg%' AND pg_tables.tablename NOT LIKE 'sql_%' AND pg_tables.tablename NOT LIKE 'ACT_%'; ");

        if (StringUtils.isNotBlank(tableName))
        {
            sql.append(" AND pg_tables.tablename = ? ");
        }

        PreparedStatement preparedStatement = super.getConnection().prepareStatement(sql.toString());

        if (StringUtils.isNotBlank(tableName))
        {
            preparedStatement.setObject(1, tableName);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<DbTableInfo> dbInfoList = new ArrayList<>();
        while (resultSet.next()){

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
        super.getConnection().close();
        preparedStatement.close();
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
        List<Map<String, Object>> list = new ArrayList<>();
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
        sql.append(" select ordinal_position as Colorder,column_name as name,data_type as type, ");
        sql.append(" coalesce(character_maximum_length,numeric_precision,-1) as Length,numeric_scale as Scale, ");
        sql.append(" case is_nullable when 'NO' then 0 else 1 end as ISNULLABLE,column_default as DEFAULTVALUE, ");
        sql.append(" case when position('nextval' in column_default)>0 then 1 else 0 end as IsIdentity, ");
        sql.append(" case when b.pk_name is null then 0 else 1 end as ISKEY,c.DeText as DESCRIPTION ");
        sql.append(" from information_schema.columns  ");
        sql.append(" left join ( ");
        sql.append(" select pg_attr.attname as colname,pg_constraint.conname as pk_name from pg_constraint  ");
        sql.append(" inner join pg_class on pg_constraint.conrelid = pg_class.oid  ");
        sql.append(" inner join pg_attribute pg_attr on pg_attr.attrelid = pg_class.oid and pg_attr.attnum = pg_constraint.conkey[1]  ");
        sql.append(" inner join pg_type on pg_type.oid = pg_attr.atttypid ");
        sql.append(" where pg_class.relname = '").append(tableName).append("' and pg_constraint.contype='p' ");
        sql.append(" ) b on b.colname = information_schema.columns.column_name ");
        sql.append(" left join ( ");
        sql.append(" select attname,description as DeText from pg_class ");
        sql.append(" left join pg_attribute pg_attr on pg_attr.attrelid= pg_class.oid ");
        sql.append(" left join pg_description pg_desc on pg_desc.objoid = pg_attr.attrelid and pg_desc.objsubid=pg_attr.attnum ");
        sql.append(" where pg_attr.attnum>0 and pg_attr.attrelid=pg_class.oid and pg_class.relname='").append(tableName).append("'");
        sql.append(" )c on c.attname = information_schema.columns.column_name ");
        sql.append(" where table_schema='public' and table_name= ? order by ordinal_position asc ");
//        if (!tableName.isEmpty())
//        {
//            sql.append(" WHERE   ");
//            sql.append(" col.table_name =  '").append(tableName).append("'");
//        }
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
                    resultSet.getString("name"),
                    resultSet.getString("type"),
                    resultSet.getInt("iskey"),
                    resultSet.getString("defaultvalue"),
                    resultSet.getLong("length"),
                    resultSet.getInt("isnullable"),
                    resultSet.getString("description"));
            columnInfos.add(model);
        }
        super.getConnection().close();
        preparedStatement.close();
        return  columnInfos;
    }

    @Override
    public List<Map<String, Object>> getDataByIdAndTableName(String tableName) throws SQLException {

        //用语句包一层 可以保证sql 只查空数据  可以得到列名就行 不需要数据
        String sql = "SELECT * FROM  " + tableName + " LIMIT 100 OFFSET 0" ;

        PreparedStatement preparedStatement = super.getConnection().prepareStatement(sql);

//        if (StringUtils.isNotBlank(tableName))
//        {
//            preparedStatement.setObject(0,  tableName);
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