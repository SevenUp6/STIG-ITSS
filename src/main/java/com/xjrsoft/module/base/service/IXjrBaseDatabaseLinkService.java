package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.DatabaselinkDto;
import com.xjrsoft.module.base.dto.DbTableListDto;
import com.xjrsoft.module.base.dto.TestConnectionDto;
import com.xjrsoft.module.base.entity.XjrBaseDatabaselink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据库连接表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface IXjrBaseDatabaseLinkService extends IService<XjrBaseDatabaselink> {


    Boolean testConnection(TestConnectionDto dto) throws SQLException, ClassNotFoundException;

    List<DbTableInfo> getTablesById(String id, String tableName, IPage<DbTableInfo> page) throws Exception;

    List<Map<String,Object>>  getDataById(String id, String tableName) throws Exception;

    List<String> getFieldById(String id,String sql) throws Exception;

    List<TableColumnInfo> getFieldByIdAndTableName(String id, String tableName) throws Exception;

    List<Map<String,Object>> getDataByIdAndTableName(String id, String tableName) throws Exception;

    Boolean updateDatabaseLink(String id, XjrBaseDatabaselink dbLink);

    Boolean addDatabaseLink(XjrBaseDatabaselink dbLink);

    Boolean deleteDatabaseLink(String ids);

    List<Map<String,Object>> getDataByColumns(String id, String columns, String sql);

    Boolean createTable(String dbLinkId, TableInfo tableInfo);

    /**
     * 查询存在的数据库表
     * @param dbId 数据库连接id
     * @param tableName 需要验证的表
     * @return
     */
    List<String> checkTableNames(String dbId, List<String> tableName);
}
