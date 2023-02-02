package com.xjrsoft.common.helper;

import com.baomidou.mybatisplus.annotation.DbType;
import com.xjrsoft.common.dbmodel.LocalDb;
import com.xjrsoft.core.tool.utils.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @title 数据库帮助类 工厂
 * @desc  用于创建各个类型数据库帮助类，
 * @author tzx
 * @create 2020年11月7日 14:27:19
 * */
public class DbHelperFactory {
    //    static {
//        ConcurrentHashMap<DbType,DbHelper> container = new ConcurrentHashMap<>();
//        container.put(DbType.MYSQL,)
//    }

    public  static DbHelper getDbHelper(DbType dbType, String conn, String userName, String passWord,String dbName) throws SQLException, ClassNotFoundException {
        switch (dbType){
            case MYSQL:
                return new MySqlDbHelper(dbType,conn,userName,passWord,dbName);
            case ORACLE :
            case ORACLE_12C:
                return new OracleDbHelper(dbType,conn,userName,passWord,dbName);
            case  SQL_SERVER:
                return new MSSqlDbHelper(dbType,conn,userName,passWord,dbName);
            case POSTGRE_SQL:
                return new PostgresDbHelper(dbType,conn,userName,passWord,dbName);
            default: //获取默认链接数据库
                return null;
        }
    }

    public  static DbHelper getDbHelper(DbType dbType, String dbLinkId) throws SQLException, ClassNotFoundException {
        switch (dbType){
            case MYSQL:
                return new MySqlDbHelper(dbType, dbLinkId);
            case ORACLE :
            case ORACLE_12C:
                return new OracleDbHelper(dbType, dbLinkId);
            case  SQL_SERVER:
                return new MSSqlDbHelper(dbType, dbLinkId);
            case POSTGRE_SQL:
                return new PostgresDbHelper(dbType, dbLinkId);
            default: //获取默认链接数据库
                return null;
        }
    }

    public static DbType getDbTypeByDriver(String driver){
        if (driver.toUpperCase().contains("MYSQL")){
            return DbType.MYSQL;
        }
        else if(driver.toUpperCase().contains("SQLSERVER")){
            return DbType.SQL_SERVER;
        }
        else if(driver.toUpperCase().contains("ORACLE")){
            return DbType.ORACLE;
        }
        else if(driver.toUpperCase().contains("POSTGRESQL")){
            return  DbType.POSTGRE_SQL;
        }
        else {
            return DbType.MYSQL;
        }
    }

    public static DbHelper getLocalDbHelper(LocalDb db) throws Exception {
        Map<String, String> datasourceParam = db.datasource.get(db.primary);

        DbType dbType = DbHelperFactory.getDbTypeByDriver(datasourceParam.get("driver-class-name"));


        DbHelper dbHelper = DbHelperFactory.getDbHelper(dbType, "master");
        if (dbHelper == null){
            throw new Exception();
        }
//        if(dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C || dbType == DbType.DM || dbType == DbType.SQL_SERVER)
//            return dbHelper;

        if(DbType.MYSQL != dbType)
            return dbHelper;

        Connection conn = dbHelper.getConnection();
        String dbName = conn.getSchema();
        dbHelper.setDbName(StringUtil.isEmpty(dbName) ? conn.getCatalog() : dbName);
        return dbHelper;
    }

}
