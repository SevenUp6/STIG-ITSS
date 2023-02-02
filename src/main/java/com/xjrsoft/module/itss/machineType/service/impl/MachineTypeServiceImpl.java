package com.xjrsoft.module.itss.machineType.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.serializers.LocalDateTimeDeserializer;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.machineType.entity.MachineType;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.module.itss.machineType.entity.FaultType;
import com.xjrsoft.module.itss.machineType.mapper.FaultTypeMapper;
import com.xjrsoft.module.itss.machineType.entity.MachineModule;
import com.xjrsoft.module.itss.machineType.mapper.MachineModuleMapper;
import com.xjrsoft.module.itss.machineType.dto.MachineTypeListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import com.xjrsoft.module.itss.machineType.mapper.MachineTypeMapper;
import com.xjrsoft.module.itss.machineType.service.IMachineTypeService;
import com.xjrsoft.core.mp.base.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 设备种类表 服务实现类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Service
@AllArgsConstructor
public class MachineTypeServiceImpl extends BaseService<MachineTypeMapper, MachineType> implements IMachineTypeService {

	private FaultTypeMapper faultTypeMapper;
	private MachineModuleMapper machineModuleMapper;

	@Override
	public IPage<MachineType> getPageList(MachineTypeListDto pageListDto) {
		Wrapper<MachineType> wrapper = Wrappers.<MachineType>query().lambda()				.like(!StringUtil.isEmpty(pageListDto.getMach_name()), MachineType::getMachName, pageListDto.getMach_name());
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public boolean addMachineType(MachineType machineType, List<FaultType> faultTypeList, List<MachineModule> machineModuleList) {
		String machineTypeId = IdWorker.get32UUID();
		machineType.setId(machineTypeId);
		machineType.setCreatedTime(LocalDateTime.now());
		String userId = SecureUtil.getUserId();
		machineType.setCreatedBy(userId);
//		faultTypeList.forEach(faultType -> faultType.setModId(machineTypeId));
//		this.saveBatch(faultTypeList, FaultType.class, FaultTypeMapper.class);
//		machineModuleList.forEach(machineModule -> machineModule.setTypeId(machineTypeId));
//		this.saveBatch(machineModuleList, MachineModule.class, MachineModuleMapper.class);
		boolean isSuccess = this.save(machineType);
		return isSuccess;
	}

	@Override
	public boolean updateMachineType(String id, MachineType machineType, List<FaultType> faultTypeList, List<MachineModule> machineModuleList) {
		machineType.setId(id);
		machineType.setUpdatedTime(LocalDateTime.now());
		String userId = SecureUtil.getUserId();
		machineType.setUpdatedBy(userId);
//		faultTypeList.forEach(faultType -> faultType.setModId(id));
//		faultTypeMapper.delete(Wrappers.<FaultType>query().lambda().eq(FaultType::getModId, id));
//		this.saveBatch(faultTypeList, FaultType.class, FaultTypeMapper.class);
//		machineModuleList.forEach(machineModule -> machineModule.setTypeId(id));
//		machineModuleMapper.delete(Wrappers.<MachineModule>query().lambda().eq(MachineModule::getTypeId, id));
		this.saveBatch(machineModuleList, MachineModule.class, MachineModuleMapper.class);
		return this.updateById(machineType);
	}

	public List<FaultType> getFaultTypeByParentId(String parentId){
		Wrapper wrapper = Wrappers.<FaultType>query().lambda().eq(FaultType::getModId, parentId);
		return faultTypeMapper.selectList(wrapper);
	}
	public List<MachineModule> getMachineModuleByParentId(String parentId){
		Wrapper wrapper = Wrappers.<MachineModule>query().lambda().eq(MachineModule::getTypeId, parentId);
		return machineModuleMapper.selectList(wrapper);
	}

	public List<MachineType> getListByName(String name){
		Wrapper wrapper = Wrappers.<MachineType>query().lambda().eq(!StringUtil.isEmpty(name),MachineType::getMachName, name);
		return this.list(wrapper);
	}
}
