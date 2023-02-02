package com.xjrsoft.module.itss.machineModule.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存设备模块表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
public class SaveMachineModuleFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("machine_moduleEntity")
    private MachineModuleDto machineModuleDto;

    @JsonProperty("fault_typeEntityList")
    private List<FaultTypeDto> faultTypeDto;


}
