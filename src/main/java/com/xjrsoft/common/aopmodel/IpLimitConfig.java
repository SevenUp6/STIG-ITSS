package com.xjrsoft.common.aopmodel;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class IpLimitConfig {
    private Boolean Enabled;

    private List<String> whiteIpList;

    private List<String> whiteApiList;

    private Integer time;

    private Integer hits;
}
