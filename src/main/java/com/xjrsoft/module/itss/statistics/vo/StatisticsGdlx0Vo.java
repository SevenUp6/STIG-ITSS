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
public class StatisticsGdlx0Vo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("type_id")
	private String typeId;

	@Excel(name = "维修人员名称", width = 30)
	@JsonProperty("repair_usrname")
	private String repairUsrname;

	//完成
	@Excel(name = "已完成工单数", width = 30)
	@JsonProperty("count1")
	private String count;
	//未完成
	@Excel(name = "未完成工单数", width = 30)
	@JsonProperty("count2")
	private String count2;


}
