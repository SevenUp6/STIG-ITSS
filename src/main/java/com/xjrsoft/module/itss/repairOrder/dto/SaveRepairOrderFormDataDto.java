package com.xjrsoft.module.itss.repairOrder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存维修工单表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
public class SaveRepairOrderFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("repair_orderEntity")
    private RepairOrderDto repairOrderDto;


}
