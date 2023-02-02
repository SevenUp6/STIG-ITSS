package com.xjrsoft.module.itss.repairOrder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.page.PageInput;
import lombok.Data;
    import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * 维修工单表视图实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
    @ApiModel(value = "列表RepairOrderDto对象", description = "维修工单表")
public class RepairOrderListDto extends PageInput {

    //@JsonProperty("isurgent")
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
    private String created_time;


    //@JsonProperty("repair_usrname")
    private String repair_usrname;

    private String repair_usrid;


    //@JsonProperty("assign_time_Start")
    private String assign_time_Start;

    //@JsonProperty("assign_time_End")
    private String assign_time_End;


    //@JsonProperty("repair_time")
    private String repair_time;


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

    private String code;

}
