package com.xjrsoft.common.config;

//import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
//import lombok.AllArgsConstructor;
//import org.flowable.engine.ProcessEngineConfiguration;
//import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
//import org.flowable.spring.boot.EngineConfigurationConfigurer;
//import org.flowable.spring.boot.FlowableProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;

/**
 * @Author:湘北智造-框架开发组
 * @Date:2020/10/22
 * @Description:工作流配置类
 */
//@Configuration
//@AllArgsConstructor
//@EnableConfigurationProperties(FlowableProperties.class)
//public class FlowableConfiguration implements EngineConfigurationConfigurer<StandaloneProcessEngineConfiguration> {
//    private final FlowableProperties flowableProperties;
//
//    @Override
//    public void configure(StandaloneProcessEngineConfiguration engineConfiguration) {
//        // 设置字体
//        engineConfiguration.setActivityFontName("宋体");
//        engineConfiguration.setLabelFontName("宋体");
//        engineConfiguration.setAnnotationFontName("宋体");
//        engineConfiguration.setDatabaseSchema("TZX");
//        //
////        MysqlDataSource mysqlDataSource = new MysqlDataSource();
////        mysqlDataSource.setURL("jdbc:mysql://localhost:3306/springboot?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true");
////        mysqlDataSource.setUser("root");
////        mysqlDataSource.setPassword("admin");
//
//        ProcessEngineConfiguration processEngineConfiguration = engineConfiguration.setDataSource(DataSourceUtil.getDataSource("master"));
//        processEngineConfiguration.buildProcessEngine();
//    }
//
//
//
//}
