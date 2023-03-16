package com.xjrsoft.module.itss.repairOrder.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.Enum.TransDataType;
import com.xjrsoft.common.annotation.DataTrans;
import com.xjrsoft.common.annotation.Excel;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * 维修工单表视图实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
@ApiModel(value = "列表RepairOrderVo对象", description = "维修工单表")
public class RepairOrderListVo {

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
    @Excel(name = "是否加急", readConverterExp = "1=加急,0=否")
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
     * 填报人员姓名
     */
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
    @JsonProperty("repair_usrname")
    private String repairUsrname;
    /**
     * 维修地址
     */
    @JsonProperty("repair_path")
    private String repairPath;
    /**
     * 指派时间
     */
    @JsonProperty("assign_time")
    @Excel(name = "指派时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignTime;
    /**
     * 维修时间
     */
    @JsonProperty("repair_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     * 所属公司名称
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
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

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

    /**
     * 所属公司全称
     */
    @JsonProperty("full_comcode")
    private String fullComcode;
}
