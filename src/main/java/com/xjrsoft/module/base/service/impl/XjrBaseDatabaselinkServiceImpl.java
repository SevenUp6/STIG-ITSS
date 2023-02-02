package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.xjrsoft.common.core.SplicingConnectionString;
import com.xjrsoft.common.dbmodel.DbExecutor;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.LocalDb;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.common.dbmodel.utils.SqlUtil;
import com.xjrsoft.common.helper.DbHelper;
import com.xjrsoft.common.helper.DbHelperFactory;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DbUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.DatabaselinkDto;
import com.xjrsoft.module.base.dto.DbTableListDto;
import com.xjrsoft.module.base.dto.TestConnectionDto;
import com.xjrsoft.module.base.entity.XjrBaseDatabaselink;
import com.xjrsoft.module.base.mapper.XjrBaseDatabaselinkMapper;
import com.xjrsoft.module.base.service.IXjrBaseDatabaseLinkService;
import com.xjrsoft.module.base.utils.DataBaseUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * 数据库连接表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Service
public class XjrBaseDatabaselinkServiceImpl extends ServiceImpl<XjrBaseDatabaselinkMapper, XjrBaseDatabaselink> implements IXjrBaseDatabaseLinkService {

    @Value("${xjrsoft.global-config.localDbString}")
    private String localDbString;

    @Autowired
    private LocalDb db;

    @Resource
    private DynamicRoutingDataSource routingDataSource;

    @Autowired
    private DynamicDataSourceCreator dynamicDataSourceCreator;

    @Autowired
    private DbExecutor dbExecutor;

    @Override
    @SneakyThrows
    public Boolean testConnection(TestConnectionDto dto)  {
      DbHelper dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(dto.getF_DbType()), SplicingConnectionString.getConnectionString(DbType.getDbType(dto.getF_DbType()), dto.getF_ServerAddress(), dto.getF_Version(), dto.getF_DBName()),dto.getF_DBUserName(),dto.getF_DBPwd(),dto.getF_DBName());
        if (dbHelper == null){
            return  false;
        }
        return dbHelper.testConnection();
    }

    @Override
    @SneakyThrows
    public List<DbTableInfo> getTablesById(String id, String tableName, IPage<DbTableInfo> page) {
        DbHelper dbHelper;
        if(localDbString.equals(id)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        } else {
            XjrBaseDatabaselink xjrBaseDatabaselink = baseMapper.selectById(id);
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(xjrBaseDatabaselink.getDbType()), id);
        }
       if (dbHelper == null){
            throw new Exception();
        }
       if (!StringUtil.isEmpty(tableName)) {
           tableName = "%" + tableName + "%";
       }
        PageUtil.setFirstPageNo(1);
        List<DbTableInfo> dbTableInfoList = dbHelper.getDbTableInfo(tableName, "", page);
        return dbTableInfoList;
    }

    @Override
    @SneakyThrows
    public List<Map<String,Object>> getDataById(String id, String sql) {
        DbHelper dbHelper;
        if(localDbString.equals(id)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        }
        else {
            XjrBaseDatabaselink xjrBaseDatabaselink = baseMapper.selectById(id);
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(xjrBaseDatabaselink.getDbType()), id);
        }
        if (dbHelper == null){
            throw new Exception();
        }
        return  dbHelper.getTableData(sql,"");
    }

    @Override
    @SneakyThrows
    public List<String> getFieldById(String id, String sql)  {
        DbHelper dbHelper;
        if(localDbString.equals(id)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        }
        else {
            XjrBaseDatabaselink xjrBaseDatabaselink = baseMapper.selectById(id);
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(xjrBaseDatabaselink.getDbType()),xjrBaseDatabaselink.getDbConnection(),xjrBaseDatabaselink.getDbUserName(),xjrBaseDatabaselink.getDbPwd(),xjrBaseDatabaselink.getDbName());
        }
        if (dbHelper == null){
            throw new Exception();
        }

        return dbHelper.getTableField(sql,"");
    }

    @Override
    @SneakyThrows
    public List<TableColumnInfo> getFieldByIdAndTableName(String id, String tableName)  {
        DbHelper dbHelper;
        if(localDbString.equals(id)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        }
        else {
            XjrBaseDatabaselink xjrBaseDatabaselink = this.getById(id);
            if (xjrBaseDatabaselink == null) {
            	throw new Exception("数据库连接不存在！");
            }
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(xjrBaseDatabaselink.getDbType()),xjrBaseDatabaselink.getDbConnection(),xjrBaseDatabaselink.getDbUserName(),xjrBaseDatabaselink.getDbPwd(),xjrBaseDatabaselink.getDbName());
        }
        if (dbHelper == null){
            throw new Exception();
        }

        return dbHelper.getTableFieldByName(tableName);
    }

    @Override
    @SneakyThrows
    public List<Map<String,Object>> getDataByIdAndTableName(String id, String tableName) {
        DbHelper dbHelper;
        if(localDbString.equals(id)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        }
        else {
            XjrBaseDatabaselink xjrBaseDatabaselink = this.getById(id);
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(xjrBaseDatabaselink.getDbType()),xjrBaseDatabaselink.getDbConnection(),xjrBaseDatabaselink.getDbUserName(),xjrBaseDatabaselink.getDbPwd(),xjrBaseDatabaselink.getDbName());
        }
        if (dbHelper == null) {
            throw new Exception();
        }

        return dbHelper.getDataByIdAndTableName(StringUtil.trimAllWhitespace(tableName));
    }

    @Override
    public Boolean updateDatabaseLink(String id, XjrBaseDatabaselink dbLink) {
        dbLink.setDatabaseLinkId(id);
        dbLink.setDbConnection(DataBaseUtil.buildDbConnUrl(dbLink.getServerAddress(), dbLink.getVersion(), dbLink.getDbName(), dbLink.getDbType()));
        // 删除动态数据源
        routingDataSource.removeDataSource(id);
        // 添加动态数据源
        addDynamicDataSource(dbLink);
        return this.updateById(dbLink);
    }

    @Override
    @Transactional
    public Boolean addDatabaseLink(XjrBaseDatabaselink dbLink) {
        dbLink.setDbConnection(DataBaseUtil.buildDbConnUrl(dbLink.getServerAddress(), dbLink.getVersion(), dbLink.getDbName(), dbLink.getDbType()));
        // 添加动态数据源
        boolean isSuccess = this.save(dbLink);
        if (isSuccess) {
            addDynamicDataSource(dbLink);
        }
        return isSuccess;
    }

    @Override
    public Boolean deleteDatabaseLink(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            // 删除动态数据源
            routingDataSource.removeDataSource(id);
        }
        return this.removeByIds(idList);
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> getDataByColumns(String id, String columns, String sql) {
        DbHelper dbHelper = null;
        //如果是选择的本地数据库
        if (StringUtil.equalsIgnoreCase(id, localDbString)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        } else {
            XjrBaseDatabaselink dbLink = this.getById(id);
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(dbLink.getDbType()),dbLink.getDbConnection(),dbLink.getDbUserName(),dbLink.getDbPwd(),dbLink.getDbName());
        }

        return dbHelper.getDataMapBySqlAndColumns(sql, columns);
    }

    @Override
    @SneakyThrows
    public Boolean createTable(String dbLinkId, TableInfo tableInfo) {
        DbType dbType = null;
        if(StringUtil.equalsIgnoreCase(localDbString, dbLinkId)){
            Map<String, String> datasourceParam = db.datasource.get(db.primary);
            dbType = DbHelperFactory.getDbTypeByDriver(datasourceParam.get("driver-class-name"));
        } else {
            XjrBaseDatabaselink dbLink = this.getById(dbLinkId);
            dbType = DbType.getDbType(dbLink.getDbType());
        }
        List<String> sqlList = SqlUtil.buildCreateTableSql(dbType, tableInfo);
        for (String sql : sqlList) {
            if (StringUtil.isBlank(sql)) continue;
            try {
                dbExecutor.executeUpdate(dbLinkId, sql);
            } catch (SQLException e) {
                log.error("执行建表语句失败！", e);
                throw new SQLException("SQL配置错误，请检查配置！");
            }
        }
        return true;
    }

    @Override
    @SneakyThrows
    public List<String> checkTableNames(String dbId, List<String> tableNames) {
        DbHelper dbHelper;
        if(localDbString.equals(dbId)){
            dbHelper = DbHelperFactory.getLocalDbHelper(db);
        } else {
            XjrBaseDatabaselink xjrBaseDatabaselink = baseMapper.selectById(dbId);
            dbHelper = DbHelperFactory.getDbHelper(DbType.getDbType(xjrBaseDatabaselink.getDbType()), dbId);
        }
        return dbHelper.checkTableNames(tableNames);
    }

    /**
     * 添加动态数据源
     * @param dbLink
     */
    private void addDynamicDataSource(XjrBaseDatabaselink dbLink){
        String driver = DbUtil.getDriverByType(dbLink.getDbType());
        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(dbLink.getDbUserName());
        property.setPassword(dbLink.getDbPwd());
        property.setUrl(dbLink.getDbConnection());
        property.setDriverClassName(driver);


        DruidConfig druidConfig = property.getDruid();
        druidConfig.setInitialSize(5);
        druidConfig.setMinIdle(5);
        druidConfig.setMaxActive(30);
        druidConfig.setMaxWait(60000L);
        druidConfig.setTimeBetweenEvictionRunsMillis(60000L);
        druidConfig.setMinEvictableIdleTimeMillis(300000L);


        if(StringUtil.equals(dbLink.getDbType(),"oracle")){
            Properties info = new Properties();
            info.setProperty("serverEncoding","ISO-8859-1"); //服务端编码
            info.setProperty("clientEncoding","GBK");
            druidConfig.setConnectionProperties(info);
            druidConfig.setValidationQuery("select 1 from dual");
        }else {
            druidConfig.setValidationQuery("SELECT 1");
        }

        druidConfig.setTestWhileIdle(true);
        druidConfig.setTestOnBorrow(false);
        druidConfig.setTestOnReturn(false);
        druidConfig.setPoolPreparedStatements(true);
        druidConfig.setMaxPoolPreparedStatementPerConnectionSize(20);

//        HikariCpConfig hikari = property.getHikari();
//        hikari.setIsReadOnly(false);
//        hikari.setConnectionTimeout(60000L);
//        hikari.setIdleTimeout(20000L);
//        hikari.setValidationTimeout(3000L);
//        hikari.setMaxLifetime(60000L);
//        hikari.setMaxPoolSize(60);
//        hikari.setMinIdle(10);

        DataSource dataSource = dynamicDataSourceCreator.createBasicDataSource(property);
        routingDataSource.addDataSource(dbLink.getDatabaseLinkId(), dataSource);
    }
}
