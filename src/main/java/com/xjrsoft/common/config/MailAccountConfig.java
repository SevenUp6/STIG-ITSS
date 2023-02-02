package com.xjrsoft.common.config;

import cn.hutool.extra.mail.MailAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 邮箱账户配置类
 */

@Configuration
public class MailAccountConfig {
    @Value("${xjrsoft.global-config.Email.host}")
    public String host;

    @Value("${xjrsoft.global-config.Email.port}")
    public Integer port;

    @Value("${xjrsoft.global-config.Email.auth}")
    public Boolean auth;

    @Value("${xjrsoft.global-config.Email.from}")
    public String from;

    @Value("${xjrsoft.global-config.Email.user}")
    public String user;

    @Value("${xjrsoft.global-config.Email.pass}")
    public String pass;


    @Bean
    public MailAccount mailAccount(){
        MailAccount account = new MailAccount();
        account.setHost(host);
        account.setPort(port);
        account.setAuth(auth);
        account.setFrom(from);
        account.setUser(user);
        account.setPass(pass);
        return account;
    }
}
