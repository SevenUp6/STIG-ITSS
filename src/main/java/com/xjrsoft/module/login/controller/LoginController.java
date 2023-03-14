package com.xjrsoft.module.login.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.RedisUtil;
import com.xjrsoft.core.constant.RedisKeyConstants;
import com.xjrsoft.core.log.annotation.ApiLog;
import com.xjrsoft.core.secure.TokenInfo;
import com.xjrsoft.core.secure.props.AdminUserProperties;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseCompanyService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.base.vo.CompanyPageListVo;
import com.xjrsoft.module.base.vo.DepartmentTreeVo;
import com.xjrsoft.module.base.vo.RoleVo;
import com.xjrsoft.module.login.dto.LoginInfoDto;
import com.xjrsoft.module.login.entity.UserInfo;
import com.xjrsoft.module.login.granter.ITokenGranter;
import com.xjrsoft.module.login.granter.PhoneNoTokenGranter;
import com.xjrsoft.module.login.granter.TokenGranterBuilder;
import com.xjrsoft.module.login.granter.TokenParameter;
import com.xjrsoft.module.login.utils.SmsKit;
import com.xjrsoft.module.login.utils.TokenUtil;
import com.xjrsoft.module.login.vo.LoginSuccessVo;
import com.xjrsoft.module.login.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
@Api(value = "用户登录", tags = "登录接口")
public class LoginController {

	private RedisUtil redisUtil;

	private IXjrBaseUserService userService;

	private IXjrBaseCompanyService companyService;

	private final AdminUserProperties adminUserProperties;

	@ApiLog("登录用户验证")
	@PostMapping
	@ApiOperation(value = "登录获取token", notes = "传入账号:F_EnCode,密码:password")
//	@ApiOperation(value = "登录获取token", notes = "传入账号:account,密码:password")
	public Response<LoginSuccessVo> login(@RequestBody LoginInfoDto loginInfoDto) {
		String enCode = loginInfoDto.getAccount();
//		String account = loginInfoDto.getAccount();

		XjrBaseUser user_no=userService.getUserByEnCode(enCode);
		if (user_no == null) {
			return Response.notOk("帐号密码错误！");
		}
		String account=user_no.getAccount();
		UserInfo userInfo = null;
		boolean isAdmin = StringUtil.equalsIgnoreCase(loginInfoDto.getAccount(), adminUserProperties.getAccount());
		if (isAdmin) {
			userInfo = new UserInfo();
			userInfo.setUser(BeanUtil.copy(adminUserProperties, XjrBaseUser.class));
		} else {
			String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

			TokenParameter tokenParameter = new TokenParameter();
			tokenParameter.getArgs().set("account", account).set("password", loginInfoDto.getPassword()).set("refreshToken", loginInfoDto.getRefreshToken()).set("userType", userType);

			ITokenGranter granter = TokenGranterBuilder.getGranter(loginInfoDto.getGrantType());
			userInfo = granter.grant(tokenParameter);
		}
		
		XjrBaseUser user = userInfo.getUser();
		if (user == null) {
			return Response.notOk("帐号密码错误！");
		}
		if (!isAdmin) {
			// 查询公司
			userInfo.setCompany(companyService.getById(user.getCompanyId()));
			// 查询部门
			userInfo.setDepartments(userService.queryDepartmentsOfUser(user.getUserId()));
			// 查询角色
			userInfo.setRoles(userService.queryRolesOfUser(user.getUserId()));
		}

		if (userInfo == null) {
			return Response.notOk(TokenUtil.USER_NOT_FOUND);
		}
		TokenInfo tokenInfo = TokenUtil.createToken(userInfo);
		// 设置返回数据
		UserInfoVo userInfoVo = BeanUtil.copy(user, UserInfoVo.class);

		if(userInfo.getCompany() != null)
			userInfoVo.setCompany(BeanUtil.copy(userInfo.getCompany(), CompanyPageListVo.class));

		if(userInfo.getDepartments() != null)
		 	userInfoVo.setDepartments(BeanUtil.copyList(userInfo.getDepartments(), DepartmentTreeVo.class));

		if(userInfo.getRoles() != null)
			userInfoVo.setRoles(BeanUtil.copyList(userInfo.getRoles(), RoleVo.class));

		userInfoVo.setDingtalkid(user.getDingTalkId());
		LoginSuccessVo loginVo = new LoginSuccessVo();
		loginVo.setUserInfoVo(userInfoVo);
		loginVo.setToken(tokenInfo.getToken());
		return Response.ok(loginVo, "登录成功！");
	}



	@ApiLog("验证码登录")
	@PostMapping("/phone")
	@ApiOperation(value = "验证码登录获取token", notes = "传入手机号:account, 验证码:password")
	public Response loginOnline(@RequestBody LoginInfoDto loginInfoDto) {
		String mobile = loginInfoDto.getAccount();
		if (!SmsKit.isPhone(mobile)) {
			return Response.notOk("手机号错误！");
		}
		if (redisUtil.getExpire(RedisKeyConstants.CACHE_SMS_CODE_PREFIX + mobile) > System.currentTimeMillis()) {
			return Response.notOk("验证码已过期！");
		}
		String cacheSmsCode = Func.toStr(redisUtil.get(RedisKeyConstants.CACHE_SMS_CODE_PREFIX + mobile));
		if (!StringUtil.equals(cacheSmsCode, loginInfoDto.getPassword())) {
			return Response.notOk("验证码错误！");
		}
		loginInfoDto.setGrantType(PhoneNoTokenGranter.GRANT_TYPE);
		return login(loginInfoDto);
	}

	@GetMapping("/{mobile}")
	@ApiOperation(value = "发送验证码")
	public Response sendSms(@PathVariable String mobile) {
		String smsCode = StringPool.EMPTY;
		if (SmsKit.isPhone(mobile)) {
			smsCode = SmsKit.sendSmsByHuaWei(mobile);
		}
		Integer count = userService.count(Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getMobile, mobile));
		if (count == 0) {
			// 保存手机号码
			XjrBaseUser user = new XjrBaseUser();
			user.setMobile(mobile);
			userService.save(user);
		}
		if (StringUtil.isNotBlank(smsCode)) {
			redisUtil.set(RedisKeyConstants.CACHE_SMS_CODE_PREFIX + mobile, smsCode, 600);
			return Response.status(true);
		}
		return Response.status(false);
	}

	@PostMapping("/add-clue")
	@ApiOperation(value = "crm增加数据")
	public Response addClue() {
		return null;
	}
}