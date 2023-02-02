package com.xjrsoft.module.itss.machineModule.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.xjrsoft.module.itss.machineModule.entity.FaultType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 故障类型表视图实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "FaultTypeVO对象", description = "故障类型表")
public class FaultTypeVo {
	private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private String id;

	@JsonProperty("fau_name")
	private String fauName;
}
