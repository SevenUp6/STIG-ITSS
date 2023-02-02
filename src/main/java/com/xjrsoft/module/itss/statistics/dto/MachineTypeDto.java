package com.xjrsoft.module.itss.statistics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备种类表数据传输对象实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
public class MachineTypeDto {
	private static final long serialVersionUID = 1L;

	@JsonProperty("mach_name")
	private String machName;

	@JsonProperty("id")
	private String id;

	@JsonProperty("remark")
	private String remark;
	/**
	 * 创建人
	 */
	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("machineModules")
	private List<MachineModuleDto> machineModules;

}
