package com.xjrsoft.module.itss.statistics.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
    
/**
 * 保存设备种类表数据传输对象实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
public class StatisticsFormDataVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("machine_type")
    private StatisticsSbgzVo statisticsVo;




}
