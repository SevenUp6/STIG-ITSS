package com.xjrsoft.core.secure;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class XjrUser {

	private static final long serialVersionUID = 1L;
//	/**
//	 * 客户端id
//	 */
//	@ApiModelProperty(hidden = true)
//	private String clientId;

	/**
	 * 用户id
	 */
	@ApiModelProperty(hidden = true)
	private String userId;
//	/**
//	 * 租户ID
//	 */
//	@ApiModelProperty(hidden = true)
//	private String tenantId;
	/**
	 * 昵称
	 */
	@ApiModelProperty(hidden = true)
	private String userName;
	/**
	 * 账号
	 */
	@ApiModelProperty(hidden = true)
	private String account;
	/**
	 * 角色id
	 */
	@ApiModelProperty(hidden = true)
	private String roleId;
	/**
	 * 角色名
	 */
	@ApiModelProperty(hidden = true)
	private String roleName;

	/**
	 * 是否是超级管理员
	 */
	@ApiModelProperty(hidden = true)
	private boolean isAdminUser;
}
