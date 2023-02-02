package com.xjrsoft.module.itss.machineModule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备模块表数据传输对象实体类
 *
 * @author hanhe
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
	@JsonProperty("mod_name")
	private String modName;
	/**
	 * 设备类型id
	 */
	@JsonProperty("type_id")
	private String typeId;
	/**
	 * 设备类型名称
	 */
	@JsonProperty("type_name")
	private String typeName;
	/**
	 * 备注
	 */
	@JsonProperty("remark")
	private String remark;
	/**
	 * 创建人
	 */
	@JsonProperty("created_by")
	private String createdBy;
	/**
	 * 创建时间
	 */
	@JsonProperty("created_time")
	private LocalDateTime createdTime;
	/**
	 * 更新人
	 */
	@JsonProperty("updated_by")
	private String updatedBy;
	/**
	 * 更新时间
	 */
	@JsonProperty("updated_time")
	private LocalDateTime updatedTime;


}
