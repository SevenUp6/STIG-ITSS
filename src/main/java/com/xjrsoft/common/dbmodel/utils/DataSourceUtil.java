package com.xjrsoft.common.dbmodel.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class DataSourceUtil {

    private static final DynamicRoutingDataSource routingDataSource;

    private static final DynamicDataSourceProperties dynamicDataSourceProperties;

    static {
        routingDataSource = SpringUtil.getBean(DynamicRoutingDataSource.class);
        dynamicDataSourceProperties = SpringUtil.getBean(DynamicDataSourceProperties.class);
    }

    public static DataSource getDataSource(String dbLinkId) {
        if (StringUtil.equalsIgnoreCase(dbLinkId, "localDB")) {
            dbLinkId = dynamicDataSourceProperties.getPrimary();
        } else if (!routingDataSource.getCurrentDataSources().containsKey(dbLinkId)) {
            throw new RuntimeException("数据库连接不存在，id：" + dbLinkId);
        }
        DataSource dataSource = routingDataSource.getDataSource(dbLinkId);
        if (dataSource == null) {
            log.error("动态数据源不存在：id = " + dbLinkId);
        }
        return dataSource;
    }

    public static DataSource getDataSource() {
        String primaryKey = dynamicDataSourceProperties.getPrimary();
        return getDataSource(primaryKey);
    }
}
