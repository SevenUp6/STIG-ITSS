package com.xjrsoft.common.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.xjrsoft.common.handler.DataAuthHandler;
import com.xjrsoft.common.handler.MyLocalDateTimeTypeHandler;
import com.xjrsoft.common.interceptor.DataAuthInnerInterceptor;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.spi.schema.EnumTypeDeterminer;

import java.time.LocalDateTime;
import java.util.Properties;

//Spring boot方式
@Configuration
public class MybatisPlusConfig {

//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//        // paginationInterceptor.setOverflow(false);
//        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//        // paginationInterceptor.setLimit(500);
//        // 开启 count 的 join 优化,只针对部分 left join
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        return paginationInterceptor;
//    }

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new DataAuthInnerInterceptor(new DataAuthHandler()));
        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }



    @Bean
    public ConfigurationCustomizer getConfig(){
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
                configuration.getTypeAliasRegistry().registerAlias("EnumTypeHandler", EnumTypeHandler.class);
                configuration.getTypeAliasRegistry().registerAlias("BooleanTypeHandler", BooleanTypeHandler.class);
                configuration.getTypeAliasRegistry().registerAlias("AbstractJsonTypeHandler", AbstractJsonTypeHandler.class);
                configuration.getTypeHandlerRegistry().register(LocalDateTime.class, MyLocalDateTimeTypeHandler.class);
            }
        };
    }


    @Bean
    public DatabaseIdProvider getDatabaseIdProvider(){
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL","mysql");
        properties.setProperty("Oracle","oracle");
        properties.setProperty("PostgreSQL","postgresql");
        properties.setProperty("SQL Server","sqlserver");
        properties.setProperty("dm","dm");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}