package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 子系统表实体类
 *
 * @author Job
 * @since 2021-06-08
 */
@Data
@TableName("xjr_base_subsystem")
public class XjrBaseSubsystem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键值
	*/
	@TableId("F_Id")
	private String id;
	/**
	* 子系统名称
	*/
	@TableField("F_Name")
	private String name;
	/**
	* 编码值
	*/
	@TableField("F_EnCode")
	private String enCode;
	/**
	* 排序码
	*/
	@TableField("F_SortCode")
	private Integer sortCode;
	/**
	* 删除标记
	*/
	@TableLogic
	@TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
	private Integer deleteMark;
	/**
	* 有效标记
	*/
	@TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
	private Integer enabledMark;
	/**
	* 描述
	*/
	@TableField("F_Description")
	private String description;
	/**
	* 创建时间
	*/
	@TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
	private LocalDateTime createDate;
	/**
	* 创建人主键值
	*/
	@TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
	private String createUserId;
	/**
	* 创建人名称
	*/
	@TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
	private String createUserName;
	/**
	* 修改时间
	*/
	@TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
	private LocalDateTime modifyDate;
	/**
	* 修改人主键值
	*/
	@TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
	private String modifyUserId;
	/**
	* 修改人名称
	*/
	@TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
	private String modifyUserName;

	/**
	 * 首页地址
	 */
	@TableField("F_IndexUrl")
	private String indexUrl;
}
