package com.xjrsoft.module.itss.machineType.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.page.PageInput;
import lombok.Data;
    import io.swagger.annotations.ApiModel;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
    @ApiModel(value = "列表MachineTypeDto对象", description = "设备种类表")
public class MachineTypeListDto extends PageInput {

    //@JsonProperty("mach_name")
    private String mach_name;


}
