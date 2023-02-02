package com.xjrsoft.module.itss.statistics.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@ApiModel(value = "StatisticsGzlxVo对象", description = "故障类型统计表")
public class StatisticsGzlxVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("type_id")
	private String typeId;
	@Excel(name = "设备种类名称", width = 30)
	@JsonProperty("type_name")
	private String typeName;
	@Excel(name = "设备模块名称", width = 30)
	@JsonProperty("mod_name")
	private String modName;

	@JsonProperty("mod_id")
	private String modId;

	@Excel(name = "设备故障名称", width = 30)
	@JsonProperty("fau_name")
	private String fauName;

	@Excel(name = "数量", width = 30)
	@JsonProperty("count")
	private String count;



}
