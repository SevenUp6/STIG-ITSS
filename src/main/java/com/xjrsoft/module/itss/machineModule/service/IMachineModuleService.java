package com.xjrsoft.module.itss.machineModule.service;

import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import com.xjrsoft.module.itss.machineModule.entity.FaultType;
import com.xjrsoft.module.itss.machineModule.dto.MachineModuleListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备模块表 服务类
 *
 * @author hanhe
 * @since 2022-10-12
 */
public interface IMachineModuleService extends IService<MachineModule> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<MachineModule> getPageList(MachineModuleListDto pageListDto);

	List<FaultType> getFaultTypeByParentId(String parentId);
	List  getListByNamePid(String parentId,String name);
	boolean addMachineModule(MachineModule machineModule, List<FaultType> faultTypeList);

	boolean updateMachineModule(String id, MachineModule machineModule, List<FaultType> faultTypeList);
}
