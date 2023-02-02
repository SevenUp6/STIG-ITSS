package com.xjrsoft.module.base.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.dto.XjrBaseSubsystemListDto;
import com.xjrsoft.module.base.entity.XjrBaseSubsystem;

/**
 * 子系统表 服务类
 *
 * @author Job
 * @since 2021-06-08
 */
public interface IXjrBaseSubsystemService extends IService<XjrBaseSubsystem> {
	/**
	 * 查询列表数据
	 * @param listDto
	 * @return
	 */
	List<XjrBaseSubsystem> getList(XjrBaseSubsystemListDto listDto);

	List<XjrBaseSubsystem> getActiveList();

	boolean addXjrBaseSubsystem(XjrBaseSubsystem xjrBaseSubsystem);

	boolean updateXjrBaseSubsystem(String id, XjrBaseSubsystem xjrBaseSubsystem);

	List<XjrBaseSubsystem> getCurAuthList();
}
