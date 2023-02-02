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
    @ApiModel(value = "列表StatisticsGdmxListDto对象", description = "工单明细")
public class StatisticsGdmxListDto extends PageInput {

    private Integer isurgent;


    //@JsonProperty("status")
    private Integer status;


    //@JsonProperty("report_name")
    private String report_name;


    //@JsonProperty("report_phone")
    private String report_phone;


    //@JsonProperty("created_by")
    private String created_by;


    //@JsonProperty("created_time")
    private LocalDateTime created_time;


    //@JsonProperty("repair_usrname")
    private String repair_usrname;


    //@JsonProperty("assign_time_Start")
    private String assign_time_Start;

    //@JsonProperty("assign_time_End")
    private String assign_time_End;


    //@JsonProperty("repair_time")
    private LocalDateTime repair_time;


    //@JsonProperty("type_name")
    private String type_name;


    //@JsonProperty("mod_name")
    private String mod_name;


    //@JsonProperty("fau_name")
    private String fau_name;


    //@JsonProperty("handle_type")
    private Integer handle_type;


    //@JsonProperty("reason")
    private Integer reason;

    private String repairPath;

    private String fullComcode;


}
