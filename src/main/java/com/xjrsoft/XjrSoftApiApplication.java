package com.xjrsoft;

import com.xjrsoft.common.annotation.UniqueNameGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(/*exclude = DruidDataSourceAutoConfigure.class*/)
@MapperScan(value = "com.xjrsoft.module.**.mapper", nameGenerator = UniqueNameGenerator.class)
@ComponentScan(nameGenerator = UniqueNameGenerator.class)
@EnableScheduling
@EnableAsync
public class XjrSoftApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(XjrSoftApiApplication.class, args);
	}
}
