package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据授权表实体类
 *
 * @author job
 * @since 2021-01-27
 */
@Data
@TableName("xjr_base_dataauthorize")
public class XjrBaseDataAuthorize implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@TableId("F_Id")
	private String id;
	/**
	* 菜单主键
	*/
	@TableField("F_ModuleId")
	private String moduleId;
	/**
	 * 授权对象主键
	 */
	@TableField("F_ObjectId")
	private String objectId;
	/**
	 * 授权对象类型(1-角色，2-用户)
	 */
	@TableField("F_ObjectType")
	private Integer objectType;
	/**
	* 启用配置(0-不启用，1-启用)
	*/
	@TableField("F_EnabledMark")
	private Integer enabledMark;
	/**
	* 子级功能沿用父级配置(0-不启用，1-启用)
	*/
	@TableField("F_EnabledChildrenMark")
	private Integer enabledChildrenMark;
	/**
	* 数据配置类型（0-查看所有数据，1-仅查看本公司，2-仅查看本公司及所有下属公司，3-仅查看本部门，4-仅查看本部门及所有下属部门，5-仅查看本人，6-仅查看本人及所有下属员工）
	*/
	@TableField("F_DataSettingType")
	private Integer dataSettingType;
	/**
	* 创建日期
	*/
	@TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
	@JsonIgnore
	private LocalDateTime createDate;
	/**
	* 创建人主键值
	*/
	@TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
	private String createUserId;
	/**
	* 创建人名字
	*/
	@TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
	private String createUserName;
}
