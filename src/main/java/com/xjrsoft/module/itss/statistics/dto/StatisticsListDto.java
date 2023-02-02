package com.xjrsoft.module.itss.statistics.dto;

import com.xjrsoft.common.page.PageInput;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备种类表视图实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
    @ApiModel(value = "列表StatisticsListDto对象", description = "统计报表")
public class StatisticsListDto extends PageInput {

    private String type_id;//设备种类id
    private String mod_id;//设备模块id
    private LocalDateTime repaire_time; //维修日期
    private LocalDateTime create_time;  //填报日期


}
