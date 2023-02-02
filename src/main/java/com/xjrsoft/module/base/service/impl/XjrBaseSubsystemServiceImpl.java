package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.allenum.EnabledMarkEnum;
import com.xjrsoft.core.mp.base.BaseService;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.XjrBaseSubsystemListDto;
import com.xjrsoft.module.base.entity.XjrBaseSubsystem;
import com.xjrsoft.module.base.mapper.XjrBaseSubsystemMapper;
import com.xjrsoft.module.base.service.IXjrBaseSubsystemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 子系统表 服务实现类
 *
 * @author Job
 * @since 2021-06-08
 */
@Service
@AllArgsConstructor
public class XjrBaseSubsystemServiceImpl extends BaseService<XjrBaseSubsystemMapper, XjrBaseSubsystem> implements IXjrBaseSubsystemService {


	@Override
	public List<XjrBaseSubsystem> getList(XjrBaseSubsystemListDto listDto) {
		Wrapper<XjrBaseSubsystem> wrapper = Wrappers.<XjrBaseSubsystem>query().lambda()
				.like(!StringUtil.isEmpty(listDto.getF_Name()), XjrBaseSubsystem::getName, listDto.getF_Name())
				.orderByAsc(XjrBaseSubsystem::getSortCode);
		return this.list(wrapper);
	}

	public List<XjrBaseSubsystem> getCurAuthList() {
		boolean accessMainSystem = false;
		List<XjrBaseSubsystem> resultList = null;
		if (SecureUtil.isAdminUser()) {
			accessMainSystem = true;
			resultList =  getActiveList();
		} else {
			String userId = SecureUtil.getUserId();
			accessMainSystem  = accessMainSystem(userId);
			resultList =  this.baseMapper.selectAuthList(userId);
		}

		//构建主系统
		if (accessMainSystem) {
			List<XjrBaseSubsystem> mainResultList = new ArrayList<>();
			XjrBaseSubsystem mainSystem = new XjrBaseSubsystem();
			mainSystem.setId("0");
			mainSystem.setName("主系统");
			mainResultList.add(mainSystem);
			mainResultList.addAll(resultList);
			return mainResultList;
		}
		return resultList;
	}

	@Override
	public List<XjrBaseSubsystem> getActiveList() {
		return this.list(Wrappers.<XjrBaseSubsystem>query().lambda()
				.eq(XjrBaseSubsystem::getEnabledMark, EnabledMarkEnum.ENABLED.getCode())
				.orderByAsc(XjrBaseSubsystem::getSortCode));
	}


	@Override
	public boolean addXjrBaseSubsystem(XjrBaseSubsystem xjrBaseSubsystem) {
		boolean isSuccess = this.save(xjrBaseSubsystem);
		return isSuccess;
	}

	@Override
	public boolean updateXjrBaseSubsystem(String id, XjrBaseSubsystem xjrBaseSubsystem) {
		xjrBaseSubsystem.setId(id);
		return this.updateById(xjrBaseSubsystem);
	}

	public boolean accessMainSystem(String userId) {
		return this.baseMapper.checkMainSystemAuth(userId) > 0;
	}
}
