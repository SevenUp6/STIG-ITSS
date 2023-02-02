package com.xjrsoft.module.itss.faultType.vo;

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
 * 故障类型表视图实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "列表FaultTypeVo对象", description = "故障类型表")
public class FaultTypeListVo {

    @JsonProperty("id")
    private String id;

    /**
     * 故障名称
     */
    @JsonProperty("fau_name")
    private String fauName;
    /**
     * 设备模块id
     */
    @JsonProperty("mod_id")
    private String modId;
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
    @DataTrans(dataType = TransDataType.USER)
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
    @DataTrans(dataType = TransDataType.USER)
    @JsonProperty("updated_by")
    private String updatedBy;
    /**
     * 更新时间
     */
    @JsonProperty("updated_time")
    private LocalDateTime updatedTime;


}
