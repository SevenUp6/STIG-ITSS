package com.xjrsoft.module.itss.machineType.service;

import com.xjrsoft.module.itss.machineType.entity.MachineType;
import com.xjrsoft.module.itss.machineType.entity.FaultType;
import com.xjrsoft.module.itss.machineType.entity.MachineModule;
import com.xjrsoft.module.itss.machineType.dto.MachineTypeListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备种类表 服务类
 *
 * @author HANHE
 * @since 2022-10-12
 */
public interface IMachineTypeService extends IService<MachineType> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<MachineType> getPageList(MachineTypeListDto pageListDto);

	List  getListByName(String name);

	List<FaultType> getFaultTypeByParentId(String parentId);

	List<MachineModule> getMachineModuleByParentId(String parentId);
	boolean addMachineType(MachineType machineType, List<FaultType> faultTypeList, List<MachineModule> machineModuleList);

	boolean updateMachineType(String id, MachineType machineType, List<FaultType> faultTypeList, List<MachineModule> machineModuleList);
}
