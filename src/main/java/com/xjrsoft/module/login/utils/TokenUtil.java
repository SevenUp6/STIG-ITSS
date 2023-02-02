package com.xjrsoft.module.login.utils;

import com.xjrsoft.core.launch.constant.TokenConstant;
import com.xjrsoft.core.secure.TokenInfo;
import com.xjrsoft.core.secure.props.AdminUserProperties;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.login.entity.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenUtil {

	public final static String TENANT_HEADER_KEY = "Tenant-Id";
	public final static String DEFAULT_TENANT_ID = "000000";
	public final static String USER_TYPE_HEADER_KEY = "User-Type";
	public final static String DEFAULT_USER_TYPE = "web";
	public final static String USER_NOT_FOUND = "用户名或密码错误";
	public final static String HEADER_KEY = "Authorization";
	public final static String HEADER_PREFIX = "Basic ";
	public final static String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";

	private final static AdminUserProperties adminUserProperties;

	static {
		adminUserProperties = SpringUtil.getBean(AdminUserProperties.class);
	}


	/**
	 * 创建认证token
	 *
	 * @param userInfo 用户信息
	 * @return token
	 */
	public static TokenInfo createToken(UserInfo userInfo) {
		XjrBaseUser user = userInfo.getUser();

		//设置jwt参数
		Map<String, String> param = new HashMap<>(16);
		param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
//		param.put(TokenConstant.TENANT_ID, user.getTenantId());
		param.put(TokenConstant.USER_ID, Func.toStr(user.getUserId()));
		param.put(TokenConstant.ACCOUNT, user.getAccount());
		param.put(TokenConstant.USER_NAME, user.getRealName());
//		param.put(TokenConstant.ROLE_NAME, Func.join(userInfo.getRoles()));
		List<XjrBaseRole> roleList = userInfo.getRoles();
		if (CollectionUtil.isNotEmpty(roleList)) {
			param.put(TokenConstant.ROLE_ID, Func.join(roleList.stream().map(role -> role.getRoleId()).collect(Collectors.toList())));
			List<String> enCodeList = roleList.stream().map(role -> role.getEnCode()).collect(Collectors.toList());
			param.put(TokenConstant.ROLE_ENCODE, Func.join(enCodeList));
			param.put(TokenConstant.IS_ADMIN_USER, String.valueOf(StringUtil.equalsIgnoreCase(user.getAccount(), adminUserProperties.getAccount())
					|| enCodeList.contains(TokenConstant.SYS_ROLE_ENCODE)));
		}

		TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.ACCESS_TOKEN);
		return accessToken;
	}

	/**
	 * 创建refreshToken
	 *
	 * @param user 用户信息
	 * @return refreshToken
	 */
	private static TokenInfo createRefreshToken(XjrBaseUser user) {
		Map<String, String> param = new HashMap<>(16);
		param.put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN);
		param.put(TokenConstant.USER_ID, Func.toStr(user.getUserId()));
		return SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.REFRESH_TOKEN);
	}

}