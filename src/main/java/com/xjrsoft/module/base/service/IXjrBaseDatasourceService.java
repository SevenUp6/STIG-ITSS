package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.dto.DataSourceDto;
import com.xjrsoft.module.base.entity.XjrBaseDatasource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.vo.DataSourceVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据源表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-09
 */
public interface IXjrBaseDatasourceService extends IService<XjrBaseDatasource> {

    List<DataSourceVo> getList(String keyword);

    List<Map<String,Object>> getDataList(String id, String sql, String field, String keyword) throws Exception;

    List<String> getFields(String id, String sql) throws Exception;

    List<Map<String,Object>> getDataToTree(String id, String field, String parentfield) throws Exception;

    List<Map<String,Object>> getDataByColumns(String id, String columns) throws Exception;

    Boolean updateDatasource(String id, XjrBaseDatasource datasource);
}

