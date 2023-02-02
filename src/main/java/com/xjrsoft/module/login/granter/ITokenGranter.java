package com.xjrsoft.module.login.granter;

import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.login.entity.UserInfo;

public interface ITokenGranter {

	/**
	 * 获取用户信息
	 *
	 * @param tokenParameter 授权参数
	 * @return UserInfo
	 */
	UserInfo grant(TokenParameter tokenParameter);

}