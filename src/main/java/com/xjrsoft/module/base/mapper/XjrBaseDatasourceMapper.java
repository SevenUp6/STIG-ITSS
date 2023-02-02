package com.xjrsoft.module.base.mapper;

import com.xjrsoft.module.base.entity.XjrBaseDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.vo.DataSourceVo;

import java.util.List;

/**
 * <p>
 * 数据源表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-09
 */
public interface XjrBaseDatasourceMapper extends BaseMapper<XjrBaseDatasource> {

    List<DataSourceVo> getList(String keyword);

}
