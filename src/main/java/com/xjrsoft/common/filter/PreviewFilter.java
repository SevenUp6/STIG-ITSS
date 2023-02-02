package com.xjrsoft.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@Component
public class PreviewFilter implements Filter {

	private static List<String> keys = new ArrayList<>();

	static {
		keys.add("token");
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String path = httpServletRequest.getServletPath();
		String method = httpServletRequest.getMethod();

		String get = "GET";
		if (method.equals(get) || keys.stream().anyMatch(path::contains)) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			throw new RuntimeException("演示环境暂时无法操作！");
		}

	}

	@Override
	public void destroy() {
	}
}