package com.xjrsoft.module.itss.repairOrder.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.module.base.vo.AnnexesFileVo;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修工单表视图实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
@ApiModel(value = "RepairOrderVO对象", description = "维修工单表")
public class RepairOrderVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("isurgent")
	private Integer isurgent;

	@JsonProperty("status")
	private Integer status;

	@JsonProperty("report_name")
	private String reportName;

	@JsonProperty("report_phone")
	private String reportPhone;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("created_time")
	private LocalDateTime createdTime;

	@JsonProperty("repair_usrname")
	private String repairUsrname;

	@JsonProperty("repair_path")
	private String repairPath;

	@JsonProperty("assign_time")
	private LocalDateTime assignTime;

	@JsonProperty("repair_time")
	private LocalDateTime repairTime;

	@JsonProperty("type_name")
	private String typeName;

	@JsonProperty("mod_name")
	private String modName;

	@JsonProperty("fau_name")
	private String fauName;

	@JsonProperty("handle_type")
	private Integer handleType;

	@JsonProperty("reason")
	private Integer reason;

	/**
	 * 所属公司id
	 */
	@JsonProperty("com_id")
	private String comId;
	/**
	 * 所属公司id
	 */
	@JsonProperty("com_name")
	private String comName;

	@JsonProperty("type_id")
	private String typeId;

	@JsonProperty("mod_id")
	private String modId;

	@JsonProperty("remark")
	private String remark;

	@JsonProperty("fau_des")
	private String fauDes;

	@JsonProperty("repair_usrid")
	private String repairUsrid;

	@JsonProperty("result_des")
	private String resultDes;

	@JsonProperty("machine_sn")
	private String machineSn;

	@JsonProperty("created_name")
	private String createdName;

	@JsonProperty("report_file")
	private List<AnnexesFileVo> report_file;

	@JsonProperty("repair_file")
	private List<AnnexesFileVo> repair_file;

	/**
	 * 所属公司全称
	 */
	@JsonProperty("full_comcode")
	private String fullComcode;

}
