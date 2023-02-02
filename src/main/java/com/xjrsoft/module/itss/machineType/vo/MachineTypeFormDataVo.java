package com.xjrsoft.module.itss.machineType.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.module.itss.machineModule.vo.MachineModuleVo;
import lombok.Data;
    
/**
 * 保存设备种类表数据传输对象实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
public class MachineTypeFormDataVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("machine_type")
    private MachineTypeVo machineTypeVo;

    @JsonProperty("fault_type")
    private List<FaultTypeVo> faultTypeVoList;

    @JsonProperty("machine_module")
    private List<MachineModuleVo> machineModuleVoList;


}
