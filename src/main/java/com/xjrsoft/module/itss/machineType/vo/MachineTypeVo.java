package com.xjrsoft.module.itss.machineType.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.xjrsoft.module.itss.machineType.entity.MachineType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "MachineTypeVO对象", description = "设备种类表")
public class MachineTypeVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("mach_name")
	private String machName;

}
