package com.xjrsoft.module.login.granter;

import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.login.entity.UserInfo;
import com.xjrsoft.module.login.enums.XjrUserEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PhoneNoTokenGranter implements ITokenGranter {

    public static final String GRANT_TYPE = "phoneNo";

    private IXjrBaseUserService userService;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        String account = tokenParameter.getArgs().getStr("account");
        UserInfo userInfo = new UserInfo();
        XjrBaseUser user = null;
        if (Func.isNoneBlank(account)) {
            // 获取用户类型
            String userType = tokenParameter.getArgs().getStr("userType");
            // 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
            if (userType.equals(XjrUserEnum.WEB.getName())) {
                user = userService.getUserByMobile(account);
            } else if (userType.equals(XjrUserEnum.APP.getName())) {
                user = userService.getUserByMobile(account);
            } else {
                user = userService.getUserByMobile(account);
            }
            userInfo.setUser(user);
        }
        return userInfo;
    }
}
