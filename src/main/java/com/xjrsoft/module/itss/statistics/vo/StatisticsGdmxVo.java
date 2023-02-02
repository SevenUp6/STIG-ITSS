package com.xjrsoft.module.itss.statistics.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.Enum.TransDataType;
import com.xjrsoft.common.annotation.DataTrans;
import com.xjrsoft.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "StatisticsGdmxVo对象", description = "工单明细统计表")
public class StatisticsGdmxVo {
	private static final long serialVersionUID = 1L;


	/**
	 * 工单编号
	 */
	@Excel(name = "工单编号", width = 30)
	@JsonProperty("code")
	private String code;
	/**
	 * 故障内容描述
	 */
	@Excel(name = "故障内容描述", width = 30)
	@JsonProperty("fau_des")
	private String fauDes;
	/**
	 * 是否加急（1是，0否）
	 */
	@Excel(name = "是否加急", readConverterExp = "1=加急,0=否")
	@JsonProperty("isurgent")
	private Integer isurgent;
	/**
	 * 工单状态
	 */
	@Excel(name = "是否加急", readConverterExp = "9=处理完成,0=待处理,8=已撤销")
	@JsonProperty("status")
	private Integer status;
	/**
	 * 报修联系人
	 */
	@Excel(name = "报修联系人", width = 30)
	@JsonProperty("report_name")
	private String reportName;
	/**
	 * 报修联系人电话
	 */
	@Excel(name = "报修联系人电话", width = 30)
	@JsonProperty("report_phone")
	private String reportPhone;
	/**
	 * 填报人员姓名
	 */
	@Excel(name = "填报人员姓名", width = 30)
	@JsonProperty("created_name")
	private String createdName;
	/**
	 * 维修人员id
	 */
	@JsonProperty("repair_usrid")
	private String repairUsrid;
	/**
	 * 维修人员姓名
	 */
	@Excel(name = "维修人员名称", width = 30)
	@JsonProperty("repair_usrname")
	private String repairUsrname;
	/**
	 * 维修地址
	 */
	@Excel(name = "维修地址", width = 30)
	@JsonProperty("repair_path")
	private String repairPath;
	/**
	 * 指派时间
	 */
	@Excel(name = "指派时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("assign_time")
	private LocalDateTime assignTime;
	/**
	 * 维修时间
	 */
	@Excel(name = "维修时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("repair_time")
	private LocalDateTime repairTime;
	/**
	 * 所属设备类型id
	 */
	@JsonProperty("type_id")
	private String typeId;
	/**
	 * 所属设备类型名称
	 */
	@Excel(name = "设备类型", width = 30)
	@JsonProperty("type_name")
	private String typeName;
	/**
	 * 设备序列号
	 */
	@Excel(name = "设备序列号", width = 30)
	@JsonProperty("machine_sn")
	private String machineSn;
	/**
	 * 所属设备模块id
	 */
	@JsonProperty("mod_id")
	private String modId;
	/**
	 * 所属设备模块名称
	 */
	@Excel(name = "设备模块", width = 30)
	@JsonProperty("mod_name")
	private String modName;
	/**
	 * 故障类型id
	 */
	@JsonProperty("fau_id")
	private String fauId;
	/**
	 * 故障类型名称
	 */
	@Excel(name = "故障类型", width = 30)
	@JsonProperty("fau_name")
	private String fauName;
	/**
	 * 所属公司id
	 */
	@JsonProperty("com_id")
	private String comId;
	/**
	 * 所属公司名称
	 */
	@Excel(name = "所属公司", width = 30)
	@JsonProperty("com_name")
	private String comName;
	/**
	 * 处理结果描述
	 */
	@Excel(name = "处理结果描述", width = 30)
	@JsonProperty("result_des")
	private String resultDes;
	/**
	 * 处理方式（1维修，2更换）
	 */
	@Excel(name = "处理方式", readConverterExp = "1=维修,2=更换")
	@JsonProperty("handle_type")
	private Integer handleType;
	/**
	 * 问题原因（1-质量问题，2-人为损坏）
	 */
	@Excel(name = "问题原因", readConverterExp = "1=质量问题,2=人为损坏")
	@JsonProperty("reason")
	private Integer reason;
	/**
	 * 报修照片（附件表主键）
	 */
	@JsonProperty("report_pic")
	private String reportPic;
	/**
	 * 维修后照片（附件表主键）
	 */
	@JsonProperty("repair_pic")
	private String repairPic;
	/**
	 * 备注
	 */
	@Excel(name = "备注", width = 30)
	@JsonProperty("remark")
	private String remark;

	@DataTrans(dataType = TransDataType.USER)
	@JsonProperty("created_by")
	private String createdBy;

	@Excel(name = "填报时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("created_time")
	private LocalDateTime createdTime;

	@DataTrans(dataType = TransDataType.USER)
	@JsonProperty("updated_by")
	private String updatedBy;

	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("updated_time")
	private LocalDateTime updatedTime;

	/**
	 * 所属公司全称
	 */

	@JsonProperty("full_comcode")
	private String fullComcode;


}
