package com.xjrsoft.module.itss.machineModule.dto;

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
 * 设备模块表视图实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
    @ApiModel(value = "列表MachineModuleDto对象", description = "设备模块表")
public class MachineModuleListDto extends PageInput {

    //@JsonProperty("mod_name")
    private String mod_name;

    private String type_id;
}
