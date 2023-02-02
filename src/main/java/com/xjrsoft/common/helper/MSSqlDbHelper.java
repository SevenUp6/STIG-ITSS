package com.xjrsoft.common.helper;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.xjrsoft.common.core.SplicingConnectionString;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title sqlserver数据库帮助类
 * @desc  集成父类，
 * @author tzx
 * @create 2021年3月11日 19:09:53
 * */
public class MSSqlDbHelper extends DbHelper {

    /**
     * @param dbType   数据库类型   mybatis-plus 枚举类型
     * @param conn     连接字符串
     * @param userName 用户名
     * @param passWord 密码
     * @title 构造函数
     */
    public MSSqlDbHelper(DbType dbType, String conn, String userName, String passWord,String dbName) throws SQLException, ClassNotFoundException {
        super(dbType, conn, userName, passWord,dbName);
    }
    public MSSqlDbHelper(DbType dbType, String dbLinkId) throws SQLException, ClassNotFoundException {
        super(dbType, dbLinkId);
    }

    @Override
    public List<DbTableInfo> getDbTableInfo(String tableName,String schemeName, IPage<DbTableInfo> page) throws SQLException {

        StringBuilder selectSql = new StringBuilder();
        selectSql.append(" SELECT ");
        selectSql.append(" Table_Name=CAST(CASE WHEN C.column_id=1 THEN O.name ELSE O.name END as VARCHAR(50)), ");
        selectSql.append(" Description=CONVERT(nvarchar(200),ISNULL(CASE WHEN C.column_id=1 THEN PTB.[value] END,N'')), ");
        selectSql.append(" Table_PK=CAST(C.name as VARCHAR(50)),PrimaryKey=CAST(ISNULL(IDX.PrimaryKey,N'') as VARCHAR(50)),[IDENTITY]=CAST(CASE WHEN C.is_identity=1 THEN N'1'ELSE N'0' END as VARCHAR(50)), 0 Index_Size,0 Data_Size,'未知' Table_Rows, Type=T.name, ");
        selectSql.append(" Length=CAST(C.max_length as VARCHAR(50)),Precision=CAST(C.precision as VARCHAR(50)),Scale=CAST(C.scale as VARCHAR(50)),NullAble=CAST(CASE WHEN C.is_nullable=1 THEN N'1'ELSE N'0' END as VARCHAR(50)),ColumnDesc=CAST(ISNULL(PFD.[value],N'') as VARCHAR(50)),IndexName=CAST(ISNULL(IDX.IndexName,N'') as VARCHAR(50)),IndexSort=ISNULL(IDX.Sort,N''),Create_Date=O.Create_Date,Create_Date=O.Create_Date,Modify_Date=O.Modify_date ");

        StringBuilder fromWhereSql = new StringBuilder();
        fromWhereSql.append(" FROM sys.columns C  ");
        fromWhereSql.append(" INNER JOIN sys.objects O ON C.[object_id]=O.[object_id] AND O.type='U' AND O.is_ms_shipped=0");
        fromWhereSql.append(" INNER JOIN sys.types T ON C.user_type_id=T.user_type_id LEFT JOIN sys.default_constraints D ON C.[object_id]=D.parent_object_id AND C.column_id=D.parent_column_id AND C.default_object_id=D.[object_id] ");
        fromWhereSql.append(" LEFT JOIN sys.extended_properties PFD ON PFD.class=1  AND C.[object_id]=PFD.major_id AND C.column_id=PFD.minor_id ");
        fromWhereSql.append(" LEFT JOIN sys.extended_properties PTB ON PTB.class=1  AND PTB.minor_id=0 AND C.[object_id]=PTB.major_id ");
        fromWhereSql.append(" LEFT JOIN ");
        fromWhereSql.append("( ");
        fromWhereSql.append("  SELECT IDXC.[object_id],  IDXC.column_id, Sort=CASE INDEXKEY_PROPERTY(IDXC.[object_id],IDXC.index_id,IDXC.index_column_id,'IsDescending')  WHEN 1 THEN 'DESC' WHEN 0 THEN 'ASC' ELSE '' END, PrimaryKey=is_primary_key,IndexName=IDX.Name ");
        fromWhereSql.append("  FROM sys.indexes IDX ");
        fromWhereSql.append("  INNER JOIN sys.index_columns IDXC ON IDX.[object_id]=IDXC.[object_id] AND IDX.index_id=IDXC.index_id");
        fromWhereSql.append("  LEFT JOIN sys.key_constraints KC ON IDX.[object_id]=KC.[parent_object_id] AND IDX.index_id=KC.unique_index_id");
        fromWhereSql.append("  INNER JOIN ");
        fromWhereSql.append("  ( SELECT [object_id], Column_id, index_id=MIN(index_id) FROM sys.index_columns GROUP BY [object_id], Column_id ) IDXCUQ ON IDXC.[object_id]=IDXCUQ.[object_id]");
        fromWhereSql.append("   AND IDXC.Column_id=IDXCUQ.Column_id    AND IDXC.index_id=IDXCUQ.index_id");
        fromWhereSql.append(") IDX ON C.[object_id]=IDX.[object_id] AND C.column_id=IDX.column_id  WHERE IDX.PrimaryKey = 1");
         if (StringUtils.isNotBlank(tableName))
        {
            fromWhereSql.append(" AND O.name LIKE ? ");
        }

        StringBuilder orderBySql = new StringBuilder();
        orderBySql.append("  ORDER BY O.name,C.column_id");
        List<DbTableInfo> dbInfoList = new ArrayList<>();

        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(tableName)) {
            params.add(tableName);
        }

        String runSql = selectSql.append(fromWhereSql).append(orderBySql).toString();
        if(page != null) {
            Integer total = this.count(fromWhereSql, params);
            page.setTotal(total);
            if (total != null & total > 0) {
                // 拼装sql
                StringBuilder fullSql = selectSql.append(fromWhereSql).append(orderBySql);
                runSql = this.wrapPageSql(fullSql, page).toString();
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
        }
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
        sql.append(" SELECT [f_number] = CAST(a.colorder as VARCHAR(50)) , [Name] = CAST(a.name as VARCHAR(50)) , [Type] = CAST(b.name as VARCHAR(50)) , [Length] = CAST( COLUMNPROPERTY(a.id, a.name, 'PRECISION') as VARCHAR(50) ), [f_identity] = CAST( CASE WHEN COLUMNPROPERTY(a.id, a.name, 'IsIdentity') = 1 THEN '1' ELSE '' END as VARCHAR(50)), [IsKey] = CAST( CASE WHEN EXISTS ( SELECT 1 FROM sysobjects WHERE xtype = 'PK' AND parent_obj = a.id AND name IN ( SELECT name FROM sysindexes WHERE indid IN ( SELECT indid FROM sysindexkeys WHERE id = a.id AND colid = a.colid ) ) ) THEN '1' ELSE '0' END as VARCHAR(50) ), [IsNullable] = CAST( CASE WHEN a.isnullable = 1 THEN '1' ELSE '0' END as VARCHAR(50) ) , [DefaultValue] = CAST( ISNULL(e.text, '') as VARCHAR(50)), [Description] = CAST(ISNULL(g.[value], a.name) as VARCHAR(50) )");
        sql.append(" FROM syscolumns a LEFT JOIN systypes b ON a.xusertype = b.xusertype INNER JOIN sysobjects d ON a.id = d.id AND d.xtype = 'U' AND d.name <> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault = e.id LEFT JOIN sys.extended_properties g ON a.id = g.major_id AND a.colid = g.minor_id LEFT JOIN sys.extended_properties f ON d.id = f.major_id AND f.minor_id = 0 ");

        if (!tableName.isEmpty())
        {
            sql.append("WHERE d.name = ?");
        }
        sql.append(" ORDER BY a.id , a.colorder ");


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

        //用语句包一层 可以保证sql 只查空数据  可以得到列名就行 不需要数据
        String sql = "SELECT top 100 * FROM " + tableName;

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
        StringBuilder sql = new StringBuilder("select name as \"tableName\" from " + getDbName() + ".sys.objects where type='U' and name in(");
        for (int i = 0; i < nameNum; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }

    protected StringBuilder wrapPageSql(StringBuilder find, IPage page) {
        if (!StrUtil.containsIgnoreCase(find.toString(), "order by")) {
            find.append(" order by current_timestamp");
        }

        return find.append(" offset ").append(PageUtil.getStart((int)page.getCurrent(), (int)page.getSize())).append(" row fetch next ").append(page.getSize()).append(" row only");
    }

    public Integer count(String sql, List<Object> params) throws SQLException {
        if (!StrUtil.containsIgnoreCase(sql, "order by")) {
            sql += " order by current_timestamp";
        }
        return super.count(sql, params);
    }
}
