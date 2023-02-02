package com.xjrsoft.module.itss.machineCompany.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存设备所属单位表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
public class SaveMachineCompanyFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("machine_companyEntity")
    private MachineCompanyDto machineCompanyDto;


}
