package com.xjrsoft.module.itss.faultType.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.faultType.entity.FaultType;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.itss.faultType.mapper.FaultTypeMapper;
import com.xjrsoft.module.itss.faultType.service.IFaultTypeService;
import com.xjrsoft.core.mp.base.BaseService;
import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 故障类型表 服务实现类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Service
@AllArgsConstructor
public class FaultTypeServiceImpl extends BaseService<FaultTypeMapper, FaultType> implements IFaultTypeService {


	@Override
	public IPage<FaultType>
	getPageList(FaultTypeListDto pageListDto) {
		Wrapper<FaultType> wrapper = Wrappers.<FaultType>query().lambda()				.like(!StringUtil.isEmpty(pageListDto.getFau_name()), FaultType::getFauName, pageListDto.getFau_name())
				.like(!StringUtil.isEmpty(pageListDto.getMod_name()), FaultType::getModName, pageListDto.getMod_name())
				.eq(!StringUtil.isEmpty(pageListDto.getMod_id()), FaultType::getModId, pageListDto.getMod_id())
				.like(!StringUtil.isEmpty(pageListDto.getType_name()), FaultType::getTypeName, pageListDto.getType_name());
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public boolean addFaultType(FaultType faultType) {
		String Id = IdWorker.get32UUID();
		String userId = SecureUtil.getUserId();
		faultType.setId(Id);
		faultType.setCreatedBy(userId);
		faultType.setCreatedTime(LocalDateTime.now());
		boolean isSuccess = this.save(faultType);
		return isSuccess;
	}

	@Override
	public boolean updateFaultType(String id, FaultType faultType) {
		faultType.setId(id);
		String userId = SecureUtil.getUserId();
		faultType.setUpdatedBy(userId);
		faultType.setUpdatedTime(LocalDateTime.now());
		return this.updateById(faultType);
	}

	public List getListByNamePid(String parentId, String name){
		Wrapper<FaultType> wrapper = Wrappers.<FaultType>query().lambda()				.eq(!StringUtil.isEmpty(parentId), FaultType::getModName, parentId)
				.eq(!StringUtil.isEmpty(name), FaultType::getFauName, name);

		return this.list(wrapper);
	}

}
