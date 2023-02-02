package com.xjrsoft.module.itss.faultType.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存故障类型表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
public class SaveFaultTypeFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("fault_typeEntity")
    private FaultTypeDto faultTypeDto;


}
