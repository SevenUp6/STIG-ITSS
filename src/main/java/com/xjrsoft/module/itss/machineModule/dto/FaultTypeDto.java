package com.xjrsoft.module.itss.machineModule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.xjrsoft.module.itss.machineModule.entity.FaultType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 故障类型表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
public class FaultTypeDto {
	private static final long serialVersionUID = 1L;

}
