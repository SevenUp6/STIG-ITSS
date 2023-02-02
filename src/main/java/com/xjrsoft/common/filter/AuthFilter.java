package com.xjrsoft.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.result.ResultCode;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.buildCode.props.GlobalConfigProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
//@AllArgsConstructor
public class AuthFilter implements Filter, Ordered {
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${xjrsoft.global-config.isOpenAuth}")
	public boolean isOpenAuth;

	@Autowired
	private GlobalConfigProperties globalConfigProperties;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) servletResponse;
		res.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Accept, Origin, Authorization, content-type, ModuleId");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,PATCH,OPTIONS");
		res.setHeader("X-Powered-By","3.2.1");
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		String path = httpServletRequest.getRequestURI();
		if ("OPTIONS".equals(httpServletRequest.getMethod())) {
			httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
			return;
		}
		if (path.startsWith("/login")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		if (path.contains("swagger") || path.contains("api-docs")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// 子系统启用情况
		Boolean enabledSubSystem = globalConfigProperties.getEnabled_subSystem();
		if ((StringUtil.equalsIgnoreCase(path, "/subsystem") || StringUtil.startsWithIgnoreCase(path, "/subsystem/")) && !enabledSubSystem) {
			unAuth(servletResponse, "子系统未启用!");
			return;
		}

		if (isOpenAuth) {
			String headerToken = httpServletRequest.getHeader("Authorization");
			if (StringUtil.isBlank(headerToken)) {
				unAuth(servletResponse, "缺失令牌,鉴权失败");
				return;
			}
			Claims claims = SecureUtil.parseJWT(headerToken);
			if (claims == null) {
				unAuth(servletResponse, "请求未授权");
				return;
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

//	private boolean isSkip(String path) {
//		return AuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains)
//			|| authProperties.getSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains);
//	}

	private void unAuth(ServletResponse resp, String msg) {
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		httpResp.setStatus(HttpStatus.UNAUTHORIZED.value());
		httpResp.setHeader("Content-Type", "application/json;charset=UTF-8");
		String result = "";
		try {
			result = objectMapper.writeValueAsString(Response.notOk(ResultCode.UN_AUTHORIZED.getCode(), ResultCode.UN_AUTHORIZED.getMessage()));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		try {
			httpResp.getOutputStream().write(result.getBytes());
		} catch (IOException e) {
		}
	}

	@Override
	public int getOrder() {
		return -100;
	}
}