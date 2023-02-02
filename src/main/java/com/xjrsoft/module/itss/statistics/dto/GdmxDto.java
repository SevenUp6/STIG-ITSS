package com.xjrsoft.module.itss.statistics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.page.PageInput;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class GdmxDto  extends PageInput {
    private static final long serialVersionUID = 1L;
    /**
     * 工单id
     */
    @JsonProperty("id")
    private String id;
    /**
     * 工单编号
     */
    @JsonProperty("code")
    private String code;
    /**
     * 故障内容描述
     */
    @JsonProperty("fau_des")
    private String fauDes;
    /**
     * 是否加急（1是，0否）
     */
    @JsonProperty("isurgent")
    private Integer isurgent;
    /**
     * 工单状态
     */
    @JsonProperty("status")
    private Integer status;
    /**
     * 报修联系人
     */
    @JsonProperty("report_name")
    private String reportName;
    /**
     * 报修联系人电话
     */
    @JsonProperty("report_phone")
    private String reportPhone;
    /**
     * 填报人员id
     */
    @JsonProperty("created_by")
    private String createdBy;
    /**
     * 填报人员姓名
     */
    @JsonProperty("created_name")
    private String createdName;
    /**
     * 填报时间
     */
    @JsonProperty("created_time")
    private LocalDateTime createdTime;
    /**
     * 维修人员id
     */
    @JsonProperty("repair_usrid")
    private String repairUsrid;
    /**
     * 维修人员姓名
     */
    @JsonProperty("repair_usrname")
    private String repairUsrname;
    /**
     * 指派时间
     */
    @JsonProperty("assign_time")
    private LocalDateTime assignTime;
    /**
     * 维修时间
     */
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
    @JsonProperty("type_name")
    private String typeName;
    /**
     * 设备序列号
     */
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
    @JsonProperty("fau_name")
    private String fauName;
    /**
     * 所属公司id
     */
    @JsonProperty("com_id")
    private String comId;
    /**
     * 所属公司
     */
    @JsonProperty("com_name")
    private String comName;
    /**
     * 处理结果描述
     */
    @JsonProperty("result_des")
    private String resultDes;
    /**
     * 处理方式（1维修，2更换）
     */
    @JsonProperty("handle_type")
    private Integer handleType;
    /**
     * 问题原因（1-质量问题，2-人为损坏）
     */
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
     * 维修地址
     */
    @JsonProperty("repair_path")
    private String repairPath;

    /**
     * 所属公司全称
     */
    @JsonProperty("full_comcode")
    private String fullComcode;
}
