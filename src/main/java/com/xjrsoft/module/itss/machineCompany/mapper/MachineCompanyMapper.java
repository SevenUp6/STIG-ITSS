package com.xjrsoft.module.itss.machineCompany.mapper;

import com.xjrsoft.module.itss.machineCompany.entity.MachineCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 设备所属单位表 Mapper 接口
 *
 * @author hanhe
 * @since 2022-10-13
 */
public interface MachineCompanyMapper extends BaseMapper<MachineCompany> {

    MachineCompany getMaxComCodeByPid(String id);
}
