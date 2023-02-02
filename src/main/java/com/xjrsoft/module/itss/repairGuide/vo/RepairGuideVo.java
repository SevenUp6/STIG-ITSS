package com.xjrsoft.module.itss.repairGuide.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.Enum.TransDataType;
import com.xjrsoft.common.annotation.DataTrans;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * 维修指南表视图实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
@ApiModel(value = "RepairGuideVO对象", description = "维修指南表")
public class RepairGuideVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("guide_name")
	private String guideName;

	@JsonProperty("mod_name")
	private String modName;

	@JsonProperty("fault_name")
	private String faultName;
	@JsonProperty("mod_id")
	private String modId;

	@JsonProperty("fault_id")
	private String faultId;
	@DataTrans(dataType = TransDataType.USER)
	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("created_time")
	private LocalDateTime createdTime;

	@DataTrans(dataType = TransDataType.USER)
	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("updated_time")
	private LocalDateTime updatedTime;

	@JsonProperty("created_name")
	private String createdName;
}
