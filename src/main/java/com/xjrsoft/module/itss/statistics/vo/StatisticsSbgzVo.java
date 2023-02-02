package com.xjrsoft.module.itss.statistics.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "StatisticsSbgzVo对象", description = "设备故障统计表")
public class StatisticsSbgzVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("type_id")
	private String typeId;

	@Excel(name = "设备种类名称", width = 30)
	@JsonProperty("type_name")
	private String typeName;

	@Excel(name = "设备模块名称", width = 30)
	@JsonProperty("mod_name")
	private String modName;

	@Excel(name = "数量", width = 30)
	@JsonProperty("count")
	private String count;


}
