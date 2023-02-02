package com.xjrsoft.common.config;


import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xjrsoft.common.core.SplicingConnectionString;
import com.xjrsoft.common.serializers.LocalDateTimeDeserializer;
import com.xjrsoft.common.serializers.LocalDateTimeSerializer;
import com.xjrsoft.common.utils.DbUtil;
import com.xjrsoft.core.secure.registry.SecureRegistry;
import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Xjr配置
 *
 * @author Chill
 */
@Slf4j
@Configuration
public class XjrConfiguration implements WebMvcConfigurer {

	@Autowired(required = false)
	private DynamicDataSourceProperties dynamicDataSourceProperties;

	@Bean
	public SecureRegistry secureRegistry() {
		SecureRegistry secureRegistry = new SecureRegistry();
		secureRegistry.excludePathPatterns("/doc.html");
		secureRegistry.excludePathPatterns("/js/**");
		secureRegistry.excludePathPatterns("/webjars/**");
		secureRegistry.excludePathPatterns("/swagger-resources/**");
		secureRegistry.excludePathPatterns("/swagger/**");
		secureRegistry.excludePathPatterns("/swagger-ui.html");
		return secureRegistry;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		AntPathMatcher matcher = new AntPathMatcher();
		matcher.setCaseSensitive(false);
		configurer.setPathMatcher(matcher);
	}

	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider() {
		DataSourceProperty datasource = dynamicDataSourceProperties.getDatasource().get("master");
		return new AbstractJdbcDataSourceProvider(datasource.getDriverClassName(), datasource.getUrl(), datasource.getUsername(), datasource.getPassword()) {

			@Override
			protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
				Map<String, DataSourceProperty> map = new HashMap<>(16);
				// 子从数数据源

				ResultSet rs = statement.executeQuery("select * from xjr_base_databaselink where F_DeleteMark = 0 and F_EnabledMark = 1");
				while (rs.next()) {
					String name = rs.getString("F_DatabaseLinkId");
					String username = rs.getString("F_DBUserName");
					String password = rs.getString("F_DBPwd");
					String url = rs.getString("F_DbConnection");
					String driver = SplicingConnectionString.getDriverString(DbType.getDbType(rs.getString("F_DbType")));
					// 测试连接
					try {
						Class.forName(driver);
						DriverManager.getConnection(url, username, password);
					} catch (Exception e) {
						log.error("数据库连接失败，url：" + url, e);
						continue;
					}
					DataSourceProperty property = new DataSourceProperty();
					property.setUsername(username);
					property.setPassword(password);
					property.setUrl(url);
					property.setDriverClassName(driver);

//					DruidConfig druidConfig = property.getDruid();
//					druidConfig.setInitialSize(10);
//					druidConfig.setMinIdle(10);
//					druidConfig.setMaxActive(300);
//					druidConfig.setMaxWait(60000L);
//					druidConfig.setTimeBetweenEvictionRunsMillis(60000L);
//					druidConfig.setMinEvictableIdleTimeMillis(300000L);
//
//					if(StringUtil.equalsIgnoreCase(rs.getString("F_DbType"),"oracle")) {
//						if(StringUtil.startsWithIgnoreCase(datasource.getUrl(), "jdbc:wrap-jdbc:filters=encoding:")){
//							Properties info = new Properties();
//							info.setProperty("serverEncoding","ISO-8859-1"); //服务端编码
//							info.setProperty("clientEncoding","GBK");
//							druidConfig.setConnectionProperties(info);
//						}
//						druidConfig.setValidationQuery("select 1 from dual");
//					}
//					else{
//						druidConfig.setValidationQuery("SELECT 'X'");
//					}
//
//					druidConfig.setTestWhileIdle(true);
//					druidConfig.setTestOnBorrow(false);
//					druidConfig.setTestOnReturn(false);
//					druidConfig.setPoolPreparedStatements(false);
//					druidConfig.setMaxPoolPreparedStatementPerConnectionSize(20);


					HikariCpConfig hikari = property.getHikari();
					hikari.setIsReadOnly(false);
					hikari.setConnectionTimeout(60000L);
					hikari.setIdleTimeout(20000L);
					hikari.setValidationTimeout(3000L);
					hikari.setMaxLifetime(60000L);
					hikari.setMaxPoolSize(60);
					hikari.setMinIdle(10);
					hikari.setIsAutoCommit(true);

					map.put(name, property);
				}
				// 主数据源

				Map<String, DataSourceProperty> datasourceMap = dynamicDataSourceProperties.getDatasource();
				for (DataSourceProperty dataSourceProperty :datasourceMap.values()) {


//				DruidConfig druidConfig = dataSourceProperty.getDruid();
//				druidConfig.setInitialSize(10);
//				druidConfig.setMinIdle(10);
//				druidConfig.setMaxActive(300);
//				druidConfig.setMaxWait(60000L);
//				druidConfig.setTimeBetweenEvictionRunsMillis(60000L);
//				druidConfig.setMinEvictableIdleTimeMillis(300000L);
//
//				DbType dbType = JdbcUtils.getDbType(datasource.getUrl());
//				if((dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C)){
//					if (StringUtil.startsWithIgnoreCase(datasource.getUrl(), "jdbc:wrap-jdbc:filters=encoding:")) {
//						Properties info = new Properties();
//						info.setProperty("serverEncoding", "ISO-8859-1"); //服务端编码
//						info.setProperty("clientEncoding", "GBK");
//						druidConfig.setConnectionProperties(info);
//					}
//					druidConfig.setValidationQuery("select 1 from dual");
//				}
//				else {
//					druidConfig.setValidationQuery("SELECT 1");
//				}
//
//				druidConfig.setTestWhileIdle(true);
//				druidConfig.setTestOnBorrow(false);
//				druidConfig.setTestOnReturn(false);
//				druidConfig.setPoolPreparedStatements(false);
//				druidConfig.setMaxPoolPreparedStatementPerConnectionSize(20);

//
//				Properties connectionProperties = new Properties();
//				connectionProperties.setProperty("serverEncoding","ISO-8859-1");
//				connectionProperties.setProperty("clientEncoding","GBK");
//
//				druidConfig.setConnectionProperties(connectionProperties);

					HikariCpConfig hikari = dataSourceProperty.getHikari();
					hikari.setIsReadOnly(false);
					hikari.setConnectionTimeout(60000L);
					hikari.setIdleTimeout(20000L);
					hikari.setValidationTimeout(3000L);
					hikari.setMaxLifetime(60000L);
					hikari.setMaxPoolSize(60);
					hikari.setMinIdle(10);
					hikari.setIsAutoCommit(true);
				}
				map.putAll(datasourceMap);
				return map;
			}
		};
	}

	@Bean
	public ObjectMapper buildObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
		javaTimeModule.addSerializer(Clob.class, new ClobSerializer());
		objectMapper.registerModule(javaTimeModule);
		return objectMapper;
	}

	public class ClobSerializer extends JsonSerializer<Clob> {
		@SneakyThrows
		@Override
		public void serialize(Clob clob, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
			String value = clob.getSubString(1, (int)clob.length());
			gen.writeString(value);
		}
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		Converter<String, LocalDateTime> converter = new Converter<String, LocalDateTime>() {

			@Override
			public LocalDateTime convert(String source) {
				if (StringUtil.isNotBlank(source)) {
					if ("yyyy-MM-dd".length() == source.length()) {
						LocalDate localDate = LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						return LocalDateTime.from(localDate.atStartOfDay());
					} else if ("yyyy-MM-dd hh:mm:ss".length() == source.length()) {
						return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					}
				}
				return null;
			}
		};
		registry.addConverter(converter);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
