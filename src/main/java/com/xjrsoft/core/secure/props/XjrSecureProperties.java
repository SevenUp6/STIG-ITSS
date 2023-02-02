package com.xjrsoft.core.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("xjr.secure.url")
public class XjrSecureProperties {

	private final List<String> excludePatterns = new ArrayList<>();

}
