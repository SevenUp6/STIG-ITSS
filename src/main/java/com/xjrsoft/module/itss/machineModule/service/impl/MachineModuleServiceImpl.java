package com.xjrsoft.module.itss.machineModule.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.module.itss.machineModule.entity.FaultType;
import com.xjrsoft.module.itss.machineModule.mapper.FaultTypeMapper;
import com.xjrsoft.module.itss.machineModule.dto.MachineModuleListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;

import java.time.LocalDateTime;
import java.util.List;
import com.xjrsoft.module.itss.machineModule.mapper.MachineModuleMapper;
import com.xjrsoft.module.itss.machineModule.service.IMachineModuleService;
import com.xjrsoft.core.mp.base.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 设备模块表 服务实现类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Service
@AllArgsConstructor
public class MachineModuleServiceImpl extends BaseService<MachineModuleMapper, MachineModule> implements IMachineModuleService {

	private FaultTypeMapper faultTypeMapper;

	@Override
	public IPage<MachineModule> getPageList(MachineModuleListDto pageListDto) {
		Wrapper<MachineModule> wrapper = Wrappers.<MachineModule>query().lambda()				.like(!StringUtil.isEmpty(pageListDto.getMod_name()), MachineModule::getModName, pageListDto.getMod_name())
				.like(!StringUtil.isEmpty(pageListDto.getType_id()), MachineModule::getTypeId, pageListDto.getType_id());
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public boolean addMachineModule(MachineModule machineModule, List<FaultType> faultTypeList) {
		String machineModuleId = IdWorker.get32UUID();
		machineModule.setId(machineModuleId);
		machineModule.setCreatedTime(LocalDateTime.now());
		String userId = SecureUtil.getUserId();
		machineModule.setCreatedBy(userId);
//		faultTypeList.forEach(faultType -> faultType.setModId(machineModuleId));
//		this.saveBatch(faultTypeList, FaultType.class, FaultTypeMapper.class);
		boolean isSuccess = this.save(machineModule);
		return isSuccess;
	}

	@Override
	public boolean updateMachineModule(String id, MachineModule machineModule, List<FaultType> faultTypeList) {
		machineModule.setId(id);
		machineModule.setUpdatedTime(LocalDateTime.now());
		String userId = SecureUtil.getUserId();
		machineModule.setUpdatedBy(userId);
//		faultTypeList.forEach(faultType -> faultType.setModId(id));
//		faultTypeMapper.delete(Wrappers.<FaultType>query().lambda().eq(FaultType::getModId, id));
//		this.saveBatch(faultTypeList, FaultType.class, FaultTypeMapper.class);
		return this.updateById(machineModule);
	}

	public List<FaultType> getFaultTypeByParentId(String parentId){
		Wrapper wrapper = Wrappers.<FaultType>query().lambda().eq(FaultType::getModId, parentId);
		return faultTypeMapper.selectList(wrapper);
	}

	public List  getListByNamePid(String parentId,String name){
		Wrapper<MachineModule> wrapper = Wrappers.<MachineModule>query().lambda()				.eq(!StringUtil.isEmpty(name), MachineModule::getModName, name)
				.eq(!StringUtil.isEmpty(parentId), MachineModule::getTypeId,parentId);
		return this.list(wrapper);
	}
}
