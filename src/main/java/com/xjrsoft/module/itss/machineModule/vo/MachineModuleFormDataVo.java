package com.xjrsoft.module.itss.machineModule.vo;

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
public class MachineModuleFormDataVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("machine_module")
    private MachineModuleVo machineModuleVo;

    @JsonProperty("fault_type")
    private List<FaultTypeVo> faultTypeVoList;


}
