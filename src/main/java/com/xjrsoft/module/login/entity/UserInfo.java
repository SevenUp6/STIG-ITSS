package com.xjrsoft.module.login.entity;

import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户基础信息
	 */
	@ApiModelProperty(value = "用户")
	private XjrBaseUser user;

	/**
	 * 用户公司
	 */
	@ApiModelProperty(value = "用户公司")
	private XjrBaseCompany company;

	/**
	 * 公司部门
	 */
	@ApiModelProperty(value = "公司部门")
	private List<XjrBaseDepartment> departments;

	/**
	 * 角色集合
	 */
	@ApiModelProperty(value = "角色集合")
	private List<XjrBaseRole> roles;

}