/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.xjrsoft.core.secure.utils;

import com.xjrsoft.core.launch.constant.TokenConstant;
import com.xjrsoft.core.secure.TokenInfo;
import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.constant.SecureConstant;
import com.xjrsoft.core.secure.exception.SecureException;
import com.xjrsoft.core.secure.provider.IClientDetails;
import com.xjrsoft.core.secure.provider.IClientDetailsService;
import com.xjrsoft.core.tool.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

/**
 * Secure工具类
 *
 * @author jobob
 */
public class SecureUtil {
	private static final String XJR_USER_REQUEST_ATTR = "_XJR_USER_REQUEST_ATTR_";

	private final static String HEADER = TokenConstant.KEY_OF_HEADER_TOKEN;
	private final static String BEARER = TokenConstant.BEARER;
	private final static String ACCOUNT = TokenConstant.ACCOUNT;
	private final static String USER_ID = TokenConstant.USER_ID;
	private final static String ROLE_ID = TokenConstant.ROLE_ID;
	private final static String USER_NAME = TokenConstant.USER_NAME;
	private final static String ROLE_NAME = TokenConstant.ROLE_NAME;
	private final static String TENANT_ID = TokenConstant.TENANT_ID;
	private final static String CLIENT_ID = TokenConstant.CLIENT_ID;
	private final static Integer AUTH_LENGTH = TokenConstant.AUTH_LENGTH;
	private final static String BASIC_HEADER_PREFIX_EXT = "Basic%20";
	private static final String IS_ADMIN_USER = TokenConstant.IS_ADMIN_USER;
	private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));

	private static IClientDetailsService clientDetailsService;

//	static {
//		clientDetailsService = SpringUtil.getBean(IClientDetailsService.class);
//	}

	/**
	 * 获取用户信息
	 *
	 * @return XjrUser
	 */
	public static XjrUser getUser() {
		HttpServletRequest request = WebUtil.getRequest();
		if (request == null) {
			return null;
		}
		// 优先从 request 中获取
		Object xjrUser = request.getAttribute(XJR_USER_REQUEST_ATTR);
		if (xjrUser == null) {
			xjrUser = getUser(request);
			if (xjrUser != null) {
				// 设置到 request 中
				request.setAttribute(XJR_USER_REQUEST_ATTR, xjrUser);
			}
		}
		return (XjrUser) xjrUser;
	}

	/**
	 * 获取用户信息
	 *
	 * @param request request
	 * @return XjrUser
	 */
	public static XjrUser getUser(HttpServletRequest request) {
		Claims claims = getClaims(request);
		if (claims == null) {
			return null;
		}
//		String clientId = Func.toStr(claims.get(SecureUtil.CLIENT_ID));
		String userId = Func.toStr(claims.get(SecureUtil.USER_ID));
//		String tenantId = Func.toStr(claims.get(SecureUtil.TENANT_ID));
		String roleId = Func.toStr(claims.get(SecureUtil.ROLE_ID));
		String account = Func.toStr(claims.get(SecureUtil.ACCOUNT));
		String roleName = Func.toStr(claims.get(SecureUtil.ROLE_NAME));
		String userName = Func.toStr(claims.get(SecureUtil.USER_NAME));
		boolean isAdminUser = Func.toBoolean(claims.get(SecureUtil.IS_ADMIN_USER), false);
		XjrUser xjrUser = new XjrUser();
//		xjruser.setClientId(clientId);
		xjrUser.setUserId(userId);
//		xjruser.setTenantId(tenantId);
		xjrUser.setAccount(account);
		xjrUser.setRoleId(roleId);
		xjrUser.setRoleName(roleName);
		xjrUser.setUserName(userName);
		xjrUser.setAdminUser(isAdminUser);
		return xjrUser;
	}


	/**
	 * 获取用户id
	 *
	 * @return userId
	 */
	public static String getUserId() {
		XjrUser user = getUser();
		return (null == user) ? "" : user.getUserId();
	}

	/**
	 * 获取用户id
	 *
	 * @param request request
	 * @return userId
	 */
	public static String getUserId(HttpServletRequest request) {
		XjrUser user = getUser(request);
		return (null == user) ? "" : user.getUserId();
	}

	/**
	 * 获取用户账号
	 *
	 * @return userAccount
	 */
	public static String getUserAccount() {
		XjrUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户账号
	 *
	 * @param request request
	 * @return userAccount
	 */
	public static String getUserAccount(HttpServletRequest request) {
		XjrUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户名
	 *
	 * @return userName
	 */
	public static String getUserName() {
		XjrUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}

	/**
	 * 获取用户名
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserName(HttpServletRequest request) {
		XjrUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}

	/**
	 * 获取用户角色
	 *
	 * @return userName
	 */
	public static String getUserRole() {
		XjrUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}

	/**
	 * 获取用角色
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserRole(HttpServletRequest request) {
		XjrUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}

//	/**
//	 * 获取租户ID
//	 *
//	 * @return tenantId
//	 */
//	public static String getTenantId() {
//		XjrUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getTenantId();
//	}

//	/**
//	 * 获取租户ID
//	 *
//	 * @param request request
//	 * @return tenantId
//	 */
//	public static String getTenantId(HttpServletRequest request) {
//		XjrUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getTenantId();
//	}

//	/**
//	 * 获取客户端id
//	 *
//	 * @return tenantId
//	 */
//	public static String getClientId() {
//		XjrUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getClientId();
//	}

//	/**
//	 * 获取客户端id
//	 *
//	 * @param request request
//	 * @return tenantId
//	 */
//	public static String getClientId(HttpServletRequest request) {
//		XjrUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getClientId();
//	}

	/**
	 * 获取Claims
	 *
	 * @param request request
	 * @return Claims
	 */
	public static Claims getClaims(HttpServletRequest request) {
		String auth = request.getHeader(SecureUtil.HEADER);
		if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
			String headStr = auth.substring(0, 6).toLowerCase();
			if (headStr.compareTo(SecureUtil.BEARER) == 0) {
				auth = auth.substring(7);
				return SecureUtil.parseJWT(auth);
			}
		}
		return null;
	}

	/**
	 * 获取请求头
	 *
	 * @return header
	 */
	public static String getHeader() {
		return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
	}

	/**
	 * 获取请求头
	 *
	 * @param request request
	 * @return header
	 */
	public static String getHeader(HttpServletRequest request) {
		return request.getHeader(HEADER);
	}

	/**
	 * 解析jsonWebToken
	 *
	 * @param jsonWebToken jsonWebToken
	 * @return Claims
	 */
	public static Claims parseJWT(String jsonWebToken) {
		if (StringUtil.startsWithIgnoreCase(jsonWebToken, TokenConstant.BEARER)) {
			jsonWebToken = StringUtils.substringAfterLast(jsonWebToken, StringPool.SPACE);
		}
		try {
			return Jwts.parser()
				.setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
				.parseClaimsJws(jsonWebToken).getBody();
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 创建令牌
	 *
	 * @param user      user
	 * @param audience  audience
	 * @param issuer    issuer
	 * @param tokenType tokenType
	 * @return jwt
	 */
	public static TokenInfo createJWT(Map<String, String> user, String audience, String issuer, String tokenType) {

//		String[] tokens = extractAndDecodeHeader();
//		assert tokens.length == 2;
//		String clientId = tokens[0];
//		String clientSecret = tokens[1];

		// 获取客户端信息
//		IClientDetails clientDetails = clientDetails(clientId);

		// 校验客户端信息
//		if (!validateClient(clientDetails, clientId, clientSecret)) {
//			throw new SecureException("客户端认证失败!");
//		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
			.setIssuer(issuer)
			.setAudience(audience)
			.signWith(signatureAlgorithm, signingKey);

		//设置JWT参数
		user.forEach(builder::claim);

		//设置应用id
//		builder.claim(CLIENT_ID, clientId);

		//添加Token过期时间
		long expireMillis;
		if (tokenType.equals(TokenConstant.ACCESS_TOKEN)) {
			expireMillis = /*clientDetails.getAccessTokenValidity()*/604800 * 1000;
		} else if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
			expireMillis = /*clientDetails.getRefreshTokenValidity()*/604800 * 1000;
		} else {
			expireMillis = getExpire();
		}
		long expMillis = nowMillis + expireMillis;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp).setNotBefore(now);

		// 组装Token信息
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(builder.compact());
		tokenInfo.setExpire((int) expireMillis / 1000);

		return tokenInfo;
	}

	/**
	 * 获取过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static long getExpire() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 客户端信息解码
	 */
	@SneakyThrows
	public static String[] extractAndDecodeHeader() {
		// 获取请求头客户端信息
		String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
		header = Func.toStr(header).replace(BASIC_HEADER_PREFIX_EXT, SecureConstant.BASIC_HEADER_PREFIX);
		if (!header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
			throw new SecureException("No client information in request header");
		}
		byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new RuntimeException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, Charsets.UTF_8_NAME);
		int index = token.indexOf(StringPool.COLON);
		if (index == -1) {
			throw new RuntimeException("Invalid basic authentication token");
		} else {
			return new String[]{token.substring(0, index), token.substring(index + 1)};
		}
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader() {
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		return tokens[0];
	}

	/**
	 * 是否为系统管理员
	 * @return
	 */
	public static boolean isAdminUser() {
		XjrUser user = getUser();
		if (user != null) {
			return user.isAdminUser();
		}
		return false;
	}

//	/**
//	 * 获取客户端信息
//	 *
//	 * @param clientId 客户端id
//	 * @return clientDetails
//	 */
//	private static IClientDetails clientDetails(String clientId) {
//		return clientDetailsService.loadClientByClientId(clientId);
//	}

	/**
	 * 校验Client
	 *
	 * @param clientId     客户端id
	 * @param clientSecret 客户端密钥
	 * @return boolean
	 */
	private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
		if (clientDetails != null) {
			return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
		}
		return false;
	}

}
