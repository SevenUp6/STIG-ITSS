package com.xjrsoft.common.helper;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.core.SplicingConnectionString;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @title 数据库帮助类
 * @desc  抽象类，根据每个不同数据库实例化不同的对象
 * @author tzx
 * @create 2020年11月7日 14:27:19
 * */
@Data
public abstract class DbHelper {

    private  DbType dbType;

    private  String conn;

    private  String userName;

    private  String passWord;

    private  String dbName;

    private  Connection connection;

    private  Statement statement;

    public static final String[] filterList  = new String[]{"delete ","drop ","truncate ","update ","insert ",
            "alter ","declare ","xp_cmdshell ","exec ","execute "};

    /**
     * @title 构造函数
     * @param dbType 数据库类型   mybatis-plus 枚举类型
     * @param conn 连接字符串
     * @param userName 用户名
     * @param passWord 密码
     * */
    public DbHelper(DbType dbType,String conn,String userName,String passWord,String dbName) throws ClassNotFoundException, SQLException {
        this.dbType = dbType;
        this.conn = conn;
        this.userName = userName;
        this.passWord = passWord;
        this.dbName = dbName;

        Class.forName(SplicingConnectionString.getDriverString(this.getDbType()));
        if ((dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C) &&
                StringUtil.startsWithIgnoreCase(this.getConn(), "jdbc:wrap-jdbc:filters=encoding:")) {
            Properties info = new Properties();
            info.setProperty("user", this.getUserName());
            info.setProperty("password", this.getPassWord());
            info.setProperty("serverEncoding", "ISO-8859-1");
            info.setProperty("clientEncoding", "GBK");
        } else {
            this.connection = DriverManager.getConnection(this.getConn(), this.getUserName(), this.getPassWord());
        }
        this.statement = connection.createStatement();
    }

    public DbHelper(DbType dbType, String dbLinkId) throws ClassNotFoundException, SQLException {
        this.dbType = dbType;
        this.connection = DataSourceUtil.getDataSource(dbLinkId).getConnection();
        this.statement = connection.createStatement();
        if (!StringUtils.startsWithIgnoreCase(dbType.getDb(), DbType.ORACLE.getDb())) {
            this.dbName = StringUtil.isEmpty(dbName) ? connection.getCatalog() : dbName;
        }
    }

    /**
     * @title 根据表名 获取数据表 所有元信息
     * @param tableName 表名（逗号隔开 可以多条）
     * */
    public List<DbTableInfo> getDbTableInfo(String tableName,String schemeName) throws ClassNotFoundException, SQLException {
        return this.getDbTableInfo(tableName, schemeName, null);
    }

    /**
     * @title 根据表名 获取数据表 所有元信息，分页
     * @param tableName 表名（逗号隔开 可以多条）
     * */
    public abstract List<DbTableInfo> getDbTableInfo(String tableName,String schemeName, IPage<DbTableInfo> page) throws ClassNotFoundException, SQLException;


    /**
     * @title 根据表名 获取数据表 所有数据
     * @param schemeName 模式名（oracle 与 pg 等 数据库 需要 schemeName 可不填）
     * */
    public abstract List<Map<String,Object>>  getTableData(String sql, String schemeName) throws ClassNotFoundException, SQLException;

    /**
     * @title 根据sql 获取所有列名
     * @param schemeName 模式名（oracle 与 pg 等 数据库 需要 schemeName  可不填）
     * */
    public abstract List<String>  getTableField(String sql, String schemeName) throws ClassNotFoundException, SQLException;


    /**
     * @title 根据表名 获取列名
     * @param tableName 表名
     * */
    public abstract List<TableColumnInfo>  getTableFieldByName(String tableName) throws ClassNotFoundException, SQLException;

    /**
     * @title 根据表名 获取所有数据
     * @param tableName 表名
     * */
    public abstract List<Map<String, Object>> getDataByIdAndTableName(String tableName) throws ClassNotFoundException, SQLException;

    /**
     * @title 根据sql 获取Map数据
     * @param sql 表名
     * */
    public  abstract  List<Map<String, Object>> getDataMapBySql(String sql) throws SQLException;


    /**
     * @title 根据sql 以及 列名 获取Map数据
     * @param sql 表名
     * */
    public  abstract  List<Map<String, Object>> getDataMapBySqlAndColumns(String sql,String columns) throws SQLException;

    /**
     * @title 测试连接
     * */
    public abstract boolean testConnection() throws ClassNotFoundException, SQLException;

    public List<String> checkTableNames(List<String> tableNames) throws SQLException {
        List<String> resultList = new ArrayList<>();
        String sql = getCheckTableNamesSql(tableNames.size());
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sql);
            this.setParameters(pst, tableNames.stream().map(tableName -> StringUtils.upperCase(tableName)).collect(Collectors.toList()));
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                resultList.add(rs.getString("tableName"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (pst != null) {
                pst.close();
            }
            this.getConnection().close();
        }
        return resultList;
    }

    /**
     * 获取查询表是否存在的sql
     * @param nameNum
     * @return
     */
    public abstract String getCheckTableNamesSql(Integer nameNum);


    /**
     * 构建查询分页的sql
     * @param find
     * @param page
     * @return
     */
    protected StringBuilder wrapPageSql(StringBuilder find, IPage page){
        return find.append(" limit ").append(page.getSize()).append(" offset ").append(PageUtil.getStart((int) page.getCurrent(), (int) page.getSize()));
    }

    /**
     * 根据sql查询数据总数
     * @param sql
     * @return
     * @throws SQLException
     */
    public Integer count(String sql, List<Object> params) throws SQLException {
        String countSql = "select count(1) from (" + sql + ") t";
        StringBuilder sqlBuilder = new StringBuilder(" FROM (");
        sqlBuilder.append(sql);
        sqlBuilder.append(") t");
        return this.count(sqlBuilder, params);
    }

    public Integer count(StringBuilder sql, List<Object> params) throws SQLException {
        StringBuilder countSql = new StringBuilder("select count(1) ").append(sql);
        PreparedStatement countPst = this.getConnection().prepareStatement(countSql.toString());
        this.setParameters(countPst, params);
        ResultSet rs = countPst.executeQuery();
        Integer total = null != rs && rs.next() ? rs.getInt(1) : null;
        countPst.close();
        return total;
    }

    public void setParameters(PreparedStatement pst, List<Object> params) throws SQLException {
        if (CollectionUtil.isNotEmpty(params)) {
            int index = 1;
            for (Object param : params) {
                pst.setObject(index++, param);
            }
        }
    }
}
