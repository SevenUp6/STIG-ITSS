package com.xjrsoft.core.secure.interceptor;

import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.result.ResultCode;
import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.constant.XjrConstant;
import com.xjrsoft.core.tool.jackson.JsonUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 客户端校验
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class ClientInterceptor extends HandlerInterceptorAdapter {

	private final String clientId;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//		XjrUser user = SecureUtil.getUser();
//		if (user != null && StringUtil.equals(clientId, SecureUtil.getClientIdFromHeader()) && StringUtil.equals(clientId, user.getClientId())) {
//			return true;
//		} else {
//			log.warn("客户端认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
//			Response result = Response.notOk(ResultCode.UN_AUTHORIZED.toString());
//			response.setHeader(XjrConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
//			response.setCharacterEncoding(XjrConstant.UTF_8);
//			response.setStatus(HttpServletResponse.SC_OK);
//			try {
//				response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
//			} catch (IOException ex) {
//				log.error(ex.getMessage());
//			}
//			return false;
//		}
		return true;
	}

}
