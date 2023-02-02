package com.xjrsoft.module.login.granter;

import com.xjrsoft.core.tool.utils.DigestUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseCompanyService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.login.entity.UserInfo;
import com.xjrsoft.module.login.enums.XjrUserEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "password";

	private IXjrBaseUserService userService;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
//		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String account = tokenParameter.getArgs().getStr("account");
		String password = tokenParameter.getArgs().getStr("password");
		UserInfo userInfo = new UserInfo();
		XjrBaseUser user = null;
		if (Func.isNoneBlank(account, password)) {
			// 获取用户类型
			String userType = tokenParameter.getArgs().getStr("userType");
			// 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
			if (userType.equals(XjrUserEnum.WEB.getName())) {
				user = userService.userInfo(account, password);
			} else if (userType.equals(XjrUserEnum.APP.getName())) {
				user = userService.userInfo(account, password);
			} else {
				user = userService.userInfo(account, password);
			}
			userInfo.setUser(user);
		}
		return userInfo;
	}

}