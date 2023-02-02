package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 子系统表数据传输对象实体类
 *
 * @author Job
 * @since 2021-06-08
 */
@Data
public class XjrBaseSubsystemDto {
	private static final long serialVersionUID = 1L;

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
