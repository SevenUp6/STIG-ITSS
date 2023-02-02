package com.xjrsoft.module.login.granter;

import com.xjrsoft.core.secure.exception.SecureException;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.core.tool.utils.SpringUtil;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TokenGranterBuilder
 *
 * @author Chill
 */
@AllArgsConstructor
public class TokenGranterBuilder {

	/**
	 * TokenGranter缓存池
	 */
	private static Map<String, ITokenGranter> granterPool = new ConcurrentHashMap<>();

	static {
		granterPool.put(PasswordTokenGranter.GRANT_TYPE, SpringUtil.getBean(PasswordTokenGranter.class));
		granterPool.put(PhoneNoTokenGranter.GRANT_TYPE, SpringUtil.getBean(PhoneNoTokenGranter.class));
	}

	/**
	 * 获取TokenGranter
	 *
	 * @param grantType 授权类型
	 * @return ITokenGranter
	 */
	public static ITokenGranter getGranter(String grantType) {
		ITokenGranter tokenGranter = granterPool.get(Func.toStr(grantType, PasswordTokenGranter.GRANT_TYPE));
		if (tokenGranter == null) {
			throw new SecureException("no grantType was found");
		} else {
			return tokenGranter;
		}
	}

}