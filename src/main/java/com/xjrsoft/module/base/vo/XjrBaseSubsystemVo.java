package com.xjrsoft.module.base.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 子系统表视图实体类
 *
 * @author Job
 * @since 2021-06-08
 */
@Data
@ApiModel(value = "XjrBaseSubsystemVO对象", description = "子系统表")
public class XjrBaseSubsystemVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("F_Id")
	private String id;

	@JsonProperty("F_Name")
	private String name;

	@JsonProperty("F_EnCode")
	private String enCode;

	@JsonProperty("F_SortCode")
	private Integer sortCode;

	@JsonProperty("F_Description")
	private String description;

	/**
	 * 首页地址
	 */
	@JsonProperty("F_IndexUrl")
	private String indexUrl;
}
