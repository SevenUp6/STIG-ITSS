package com.xjrsoft.module.itss.machineType.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存设备种类表数据传输对象实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
public class SaveMachineTypeFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("machine_typeEntity")
    private MachineTypeDto machineTypeDto;

    @JsonProperty("fault_typeEntityList")
    private List<FaultTypeDto> faultTypeDto;

    @JsonProperty("machine_moduleEntityList")
    private List<MachineModuleDto> machineModuleDto;


}
