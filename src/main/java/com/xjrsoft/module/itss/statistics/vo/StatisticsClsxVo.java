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
@ApiModel(value = "StatisticsClsxVo对象", description = "处理时效统计表")
public class StatisticsClsxVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("type_id")
	private String typeId;
	@Excel(name = "维修人员名称", width = 30)
	@JsonProperty("repair_usrname")
	private String repairUsrname;

	@Excel(name = "维修时长", width = 30)
	@JsonProperty("hours")
	private String hours;


}
