package com.xjrsoft.core.secure.config;

import com.xjrsoft.core.secure.interceptor.ClientInterceptor;
import com.xjrsoft.core.secure.interceptor.SecureInterceptor;
import com.xjrsoft.core.secure.props.AdminUserProperties;
import com.xjrsoft.core.secure.props.XjrClientProperties;
import com.xjrsoft.core.secure.props.XjrSecureProperties;
import com.xjrsoft.core.secure.registry.SecureRegistry;
import com.xjrsoft.module.buildCode.props.GlobalConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.xjr.core.secure.aspect.AuthAspect;
//import com.xjr.core.secure.provider.ClientDetailsServiceImpl;

/**
 * 安全配置类
 *
 * @author Chill
 */
@Order
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({XjrSecureProperties.class, XjrClientProperties.class, GlobalConfigProperties.class, AdminUserProperties.class})
public class SecureConfiguration implements WebMvcConfigurer {

	private final SecureRegistry secureRegistry;

	private final XjrSecureProperties secureProperties;

	private final XjrClientProperties clientProperties;

//	private final JdbcTemplate jdbcTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		clientProperties.getClient().forEach(cs -> registry.addInterceptor(new ClientInterceptor(cs.getClientId())).addPathPatterns(cs.getPathPatterns()));

		if (secureRegistry.isEnable()) {
			registry.addInterceptor(new SecureInterceptor())
				.excludePathPatterns(secureRegistry.getExcludePatterns())
				.excludePathPatterns(secureRegistry.getDefaultExcludePatterns())
				.excludePathPatterns(secureProperties.getExcludePatterns());
		}
	}

//	@Bean
//	public AuthAspect authAspect() {
//		return new AuthAspect();
//	}

//	@Bean
//	@ConditionalOnMissingBean(IClientDetailsService.class)
//	public IClientDetailsService clientDetailsService() {
//		return new ClientDetailsServiceImpl(jdbcTemplate);
//	}

}
