package com.xjrsoft.module.itss.machineModule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 设备模块表视图实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "MachineModuleVO对象", description = "设备模块表")
public class MachineModuleVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("mod_name")
	private String modName;

}
