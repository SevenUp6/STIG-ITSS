package com.xjrsoft.module.itss.faultType.service;

import com.xjrsoft.module.itss.faultType.entity.FaultType;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 故障类型表 服务类
 *
 * @author hanhe
 * @since 2022-10-12
 */
public interface IFaultTypeService extends IService<FaultType> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<FaultType> getPageList(FaultTypeListDto pageListDto);
	boolean addFaultType(FaultType faultType);
	List getListByNamePid(String parentId, String name);
	boolean updateFaultType(String id, FaultType faultType);
}
