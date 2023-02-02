package com.xjrsoft.module.buildCode.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("xjrsoft.global-config")
public class GlobalConfigProperties {

    private String fontProjectPath;

    private String appProjectPath;

    private Boolean enabled_subSystem;

    private String localDbString;
}
