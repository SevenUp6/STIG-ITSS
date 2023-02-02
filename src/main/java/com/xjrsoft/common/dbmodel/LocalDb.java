package com.xjrsoft.common.dbmodel;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class LocalDb {

//    @Value("${spring.datasource.dynamic.primary}")
//    private static final String primary;
//
//
//    @Value("${spring.datasource.dynamic.primary}")
//    private String driver;
//
//    @Value("${spring.datasource.dynamic.datasource." + primary + ".driver-class-name}")
//    private String driver;
//
//    @Value("${spring.datasource.dynamic.primary}")
//    private String driver;

    public String primary;

    public Map<String,Map<String,String>> datasource = new HashMap<>();

}
