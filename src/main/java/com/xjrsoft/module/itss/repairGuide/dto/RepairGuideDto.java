package com.xjrsoft.module.itss.repairGuide.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 维修指南表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
public class RepairGuideDto {
	private static final long serialVersionUID = 1L;

	@JsonProperty("guide_name")
	private String guideName;

	@JsonProperty("mod_name")
	private String modName;

	@JsonProperty("fault_name")
	private String faultName;

	@JsonProperty("fault_id")
	private String faultId;

	@JsonProperty("mod_id")
	private String modId;

}
