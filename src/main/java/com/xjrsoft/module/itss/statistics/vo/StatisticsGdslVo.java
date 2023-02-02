package com.xjrsoft.module.itss.statistics.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "StatisticsGdslVo对象", description = "获取工单数量")
public class StatisticsGdslVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("type_name")
	private String typeName;

//总数
	@JsonProperty("sum")
	private Integer sum;
//处理完成数
	@JsonProperty("sum9")
	private Integer sum9;
//未处理数
	@JsonProperty("sum0")
	private Integer sum0;


}
