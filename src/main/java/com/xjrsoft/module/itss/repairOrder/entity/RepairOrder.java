package com.xjrsoft.module.itss.repairOrder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * 维修工单表实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 工单id
	*/
	@TableId("id")
	private String id;
	/**
	* 工单编号
	*/
	@TableField("code")
	private String code;
	/**
	* 故障内容描述
	*/
	@TableField("fau_des")
	private String fauDes;
	/**
	* 是否加急（1是，0否）
	*/
	@TableField("isurgent")
	private Integer isurgent;
	/**
	* 工单状态
	*/
	@TableField("status")
	private Integer status;
	/**
	* 报修联系人
	*/
	@TableField("report_name")
	private String reportName;
	/**
	* 报修联系人电话
	*/
	@TableField("report_phone")
	private String reportPhone;
	/**
	* 填报人员id
	*/
	@TableField("created_by")
	private String createdBy;
	/**
	* 填报人员姓名
	*/
	@TableField("created_name")
	private String createdName;
	/**
	* 填报时间
	*/
	@TableField("created_time")
	private LocalDateTime createdTime;
	/**
	* 维修人员id
	*/
	@TableField("repair_usrid")
	private String repairUsrid;
	/**
	* 维修人员姓名
	*/
	@TableField("repair_usrname")
	private String repairUsrname;
	/**
	* 维修地址
	*/
	@TableField("repair_path")
	private String repairPath;
	/**
	* 指派时间
	*/
	@TableField("assign_time")
	private LocalDateTime assignTime;
	/**
	* 维修时间
	*/
	@TableField("repair_time")
	private LocalDateTime repairTime;
	/**
	* 所属设备类型id
	*/
	@TableField("type_id")
	private String typeId;
	/**
	* 所属设备类型名称
	*/
	@TableField("type_name")
	private String typeName;
	/**
	* 设备序列号
	*/
	@TableField("machine_sn")
	private String machineSn;
	/**
	* 所属设备模块id
	*/
	@TableField("mod_id")
	private String modId;
	/**
	* 所属设备模块名称
	*/
	@TableField("mod_name")
	private String modName;
	/**
	* 故障类型id
	*/
	@TableField("fau_id")
	private String fauId;
	/**
	* 故障类型名称
	*/
	@TableField("fau_name")
	private String fauName;
	/**
	* 所属公司id
	*/
	@TableField("com_id")
	private String comId;
	/**
	* 所属公司名称
	*/
	@TableField("com_name")
	private String comName;
	/**
	* 处理结果描述
	*/
	@TableField("result_des")
	private String resultDes;
	/**
	* 处理方式（1维修，2更换）
	*/
	@TableField("handle_type")
	private Integer handleType;
	/**
	* 问题原因（1-质量问题，2-人为损坏）
	*/
	@TableField("reason")
	private Integer reason;
	/**
	* 报修照片（附件表主键）
	*/
	@TableField("report_pic")
	private String reportPic;
	/**
	* 维修后照片（附件表主键）
	*/
	@TableField("repair_pic")
	private String repairPic;
	/**
	* 备注
	*/
	@TableField("remark")
	private String remark;
	/**
	* 更新人
	*/
	@TableField("updated_by")
	private String updatedBy;
	/**
	* 更新时间
	*/
	@TableField("updated_time")
	private LocalDateTime updatedTime;
	/**
	* 更新时间
	*/
	@TableField("full_comcode")
	private String fullComcode;


}
