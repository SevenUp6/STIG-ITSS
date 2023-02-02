package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.xjrsoft.common.dbmodel.LocalDb;
import com.xjrsoft.common.helper.DbHelper;
import com.xjrsoft.common.helper.DbHelperFactory;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDatabaselink;
import com.xjrsoft.module.base.entity.XjrBaseDatasource;
import com.xjrsoft.module.base.mapper.XjrBaseDatasourceMapper;
import com.xjrsoft.module.base.service.IXjrBaseDatabaseLinkService;
import com.xjrsoft.module.base.service.IXjrBaseDatasourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.DataSourceVo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据源表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-09
 */
@Service
public class XjrBaseDatasourceServiceImpl extends ServiceImpl<XjrBaseDatasourceMapper, XjrBaseDatasource> implements IXjrBaseDatasourceService {

    @Value("${xjrsoft.global-config.localDbString}")
    private  String localDbString;

    @Autowired
    private  LocalDb db;

    @Autowired
    private IXjrBaseDatabaseLinkService databaselinkService;

    @Override
    public List<DataSourceVo> getList(String keyword) {
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        return this.baseMapper.getList(keyword);
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> getDataList(String id, String sql, String field, String keyword)  {

        XjrBaseDatasource xjrBaseDatasource = this.getById(id);

        if (xjrBaseDatasource == null){
            throw new Exception("找不到此数据源！");
        }

        DbHelper dbHelper = null;
        //如果是选择的本地数据库
        if (xjrBaseDatasource.getDbId().equals(localDbString)) {
            dbHelper = getLocalDbHelper();
        } else {
            XjrBaseDatabaselink dbLink = databaselinkService.getById(xjrBaseDatasource.getDbId());
            dbHelper = getOtherDbHelper(dbLink);
        }
        if(StringUtil.isEmpty(sql)){
            sql = xjrBaseDatasource.getFsql();
        }
        if (!StringUtil.isEmpty(field) && !StringUtil.isEmpty(keyword)) {
            sql = "SELECT * FROM (" + sql + ") temp WHERE temp." + field + " like '%" + keyword + "%'";
        }
        return dbHelper.getDataMapBySql(sql);
    }

    @Override
    @SneakyThrows
    public List<String> getFields(String id, String sql)  {
        if (StringUtil.isEmpty(sql)) {
            XjrBaseDatasource datasource = this.getById(id);
            id = datasource.getDbId();
            sql = datasource.getFsql();
        }
        DbHelper dbHelper = null;
        //如果是选择的本地数据库
        if (id.equals(localDbString)){
            dbHelper = getLocalDbHelper();
        }else {
            XjrBaseDatabaselink dbLink = databaselinkService.getById(id);
            dbHelper = getOtherDbHelper(dbLink);
        }
        return dbHelper.getTableField(sql,"");

    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> getDataToTree(String id, String field, String parentfield) {

        XjrBaseDatasource xjrBaseDatasource = this.getById(id);
        if (xjrBaseDatasource == null){
            throw new Exception("找不到此数据源！");
        }
        DbHelper dbHelper = null;
        //如果是选择的本地数据库
        if (xjrBaseDatasource.getDbId().equals(localDbString)){
            dbHelper = getLocalDbHelper();
        }else {
            XjrBaseDatabaselink dbLink = databaselinkService.getById(xjrBaseDatasource.getDbId());
            dbHelper = getOtherDbHelper(dbLink);
        }
        List<Map<String, Object>> dataMapBySql = dbHelper.getDataMapBySql(xjrBaseDatasource.getFsql());
        return ForestNodeMerger.merge(dataMapBySql,field,parentfield);
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> getDataByColumns(String id, String columns) {
        XjrBaseDatasource xjrBaseDatasource = this.getById(id);
        if (xjrBaseDatasource == null){
            throw new Exception("找不到此数据源！");
        }
        DbHelper dbHelper = null;
        //如果是选择的本地数据库
        if (xjrBaseDatasource.getDbId().equals(localDbString)){
            dbHelper = getLocalDbHelper();
        }else {
            XjrBaseDatabaselink dbLink = databaselinkService.getById(xjrBaseDatasource.getDbId());
            dbHelper = getOtherDbHelper(dbLink);
        }

        return dbHelper.getDataMapBySqlAndColumns(xjrBaseDatasource.getFsql(), columns);
    }

    @Override
    public Boolean updateDatasource(String id, XjrBaseDatasource datasource) {
        datasource.setId(id);
        return this.updateById(datasource) && OrganizationCacheUtil.updateCache(OrganizationCacheUtil.DATASOURCE_CACHE_KEY, datasource);
    }

    private DbHelper getLocalDbHelper() throws Exception {
        DbHelper dbHelper = DbHelperFactory.getLocalDbHelper(db);
        if (dbHelper == null) {
            throw new Exception();
        }
        return dbHelper;
    }

    private DbHelper getOtherDbHelper(XjrBaseDatabaselink dbLink) throws Exception {
        DbHelper dbHelper  = DbHelperFactory.getDbHelper(DbType.getDbType(dbLink.getDbType()), dbLink.getDatabaseLinkId());
        if (dbHelper == null){
            throw new Exception();
        }
        return dbHelper;
    }

    @Override
    public XjrBaseDatasource getById(Serializable id) {
        String datasourceId = String.valueOf(id);
        return OrganizationCacheUtil.getCacheById(OrganizationCacheUtil.DATASOURCE_CACHE_KEY, datasourceId);
    }
}
