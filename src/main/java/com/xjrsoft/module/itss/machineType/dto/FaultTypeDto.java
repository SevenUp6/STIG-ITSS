package com.xjrsoft.module.itss.machineType.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 故障类型表数据传输对象实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
public class FaultTypeDto {
	private static final long serialVersionUID = 1L;


	/**
	 * 故障id
	 */
	@TableId("id")
	private String id;
	/**
	 * 故障名称
	 */
	@JsonProperty("fau_name")
	private String fauName;
	/**
	 * 设备模块id
	 */
//	@JsonProperty("mod_id")
//	private String modId;
//	/**
//	 * 设备模块名称
//	 */
//	@JsonProperty("mod_name")
//	private String modName;
//	/**
//	 * 设备类型id
//	 */
//	@JsonProperty("type_id")
//	private String typeId;
//	/**
//	 * 设备类型名称
//	 */
//	@JsonProperty("type_name")
//	private String typeName;
}
