package com.xjrsoft.module.itss.machineModule.vo;

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
 * 设备模块表视图实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "列表MachineModuleVo对象", description = "设备模块表")
public class MachineModuleListVo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("mod_name")
    private String modName;

    @JsonProperty("type_name")
    private String typeName;

    @JsonProperty("type_id")
    private String typeId;

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

}
