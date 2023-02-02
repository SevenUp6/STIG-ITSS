package com.xjrsoft.core.mp.base;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.Collection;

public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    public <C extends BaseMapper, D> boolean saveBatch(Collection<D> entityList, int batchSize, Class<D> entityClass, Class<C> mapperClass) {
        String sqlStatement = SqlHelper.getSqlStatement(mapperClass, SqlMethod.INSERT_ONE);
        return SqlHelper.executeBatch(entityClass, this.log, entityList, batchSize, (sqlSession, entity) -> {
            sqlSession.insert(sqlStatement, entity);
        });
    }

    public <C extends BaseMapper, D> boolean saveBatch(Collection<D> entityList, Class<D> entityClass, Class<C> mapperClass) {
        return saveBatch(entityList, 1000, entityClass, mapperClass);
    }
}