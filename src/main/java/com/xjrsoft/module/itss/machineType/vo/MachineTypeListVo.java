package com.xjrsoft.module.itss.machineType.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.Enum.TransDataType;
import com.xjrsoft.common.annotation.DataTrans;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "列表MachineTypeVo对象", description = "设备种类表")
public class MachineTypeListVo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("mach_name")
    private String machName;

    @JsonProperty("created_by")
    @DataTrans(dataType = TransDataType.USER)
    private String createdBy;
    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    @JsonProperty("remark")
    private String remark;

}
