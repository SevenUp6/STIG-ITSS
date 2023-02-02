package com.xjrsoft.module.itss.machineType.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备模块表数据传输对象实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
public class MachineModuleDto {
	private static final long serialVersionUID = 1L;
	/**
	 * 设备模块id
	 */
	@JsonProperty("id")
	private String id;
	/**
	 * 设备模块名称
	 */
	@TableField("mod_name")
	private String modName;
	/**
	 * 设备类型id
	 */
	@TableField("type_id")
	private String typeId;
	/**
	 * 设备类型名称
	 */
	@TableField("type_name")
	private String typeName;

	@JsonProperty("faultTypes")
	private List<FaultTypeDto> faultTypes;
}
