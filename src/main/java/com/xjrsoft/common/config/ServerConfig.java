package com.xjrsoft.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    public String getLocalUrl() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("获取本地地址失败！", e);
        }
        return "http://" + localHost.getHostAddress() + ":" + this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        this.serverPort = webServerInitializedEvent.getWebServer().getPort();
    }
}
