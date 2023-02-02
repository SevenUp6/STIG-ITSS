package com.xjrsoft.module.itss.machineCompany.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.machineCompany.entity.MachineCompany;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.module.itss.machineCompany.dto.MachineCompanyListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.itss.machineCompany.mapper.MachineCompanyMapper;
import com.xjrsoft.module.itss.machineCompany.service.IMachineCompanyService;
import com.xjrsoft.core.mp.base.BaseService;
import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备所属单位表 服务实现类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Service
@AllArgsConstructor
public class MachineCompanyServiceImpl extends BaseService<MachineCompanyMapper, MachineCompany> implements IMachineCompanyService {

	private  MachineCompanyMapper machineCompanyMapper;

	@Override
	public IPage<MachineCompany> getPageList(MachineCompanyListDto pageListDto) {
		pageListDto.setSize(100);
		Wrapper<MachineCompany> wrapper = Wrappers.<MachineCompany>query().lambda()				.like(!StringUtil.isEmpty(pageListDto.getCom_name()), MachineCompany::getComName, pageListDto.getCom_name())
				.eq(MachineCompany::getPcode, pageListDto.getPcode()).orderByAsc(MachineCompany::getComName);
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public String addMachineCompany(MachineCompany machineCompany) {
		String machineCompanyId = IdWorker.get32UUID();
		machineCompany.setId(machineCompanyId);
		machineCompany.setCreatedTime(LocalDateTime.now());
		String userId = SecureUtil.getUserId();
		machineCompany.setCreatedBy(userId);
		boolean isSuccess = this.save(machineCompany);
		return isSuccess ? machineCompanyId : null;
	}

	@Override
	public boolean updateMachineCompany(String id, MachineCompany machineCompany) {
		machineCompany.setId(id);
		machineCompany.setUpdatedTime(LocalDateTime.now());
		String userId = SecureUtil.getUserId();
		machineCompany.setUpdatedBy(userId);
		return this.updateById(machineCompany);
	}

	@Override
	public MachineCompany getMaxComCodeByPid(String id){
		return machineCompanyMapper.getMaxComCodeByPid(id);

	}
	@Override
	public List getListByName(String name){
		Wrapper<MachineCompany> wrapper = Wrappers.<MachineCompany>query().lambda()				.like(!StringUtil.isEmpty(name), MachineCompany::getComName, name);
		return this.list( wrapper);
	}

}
