package com.xjrsoft.module.itss.machineCompany.service;

import com.xjrsoft.module.itss.machineCompany.entity.MachineCompany;
import com.xjrsoft.module.itss.machineCompany.dto.MachineCompanyListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备所属单位表 服务类
 *
 * @author hanhe
 * @since 2022-10-13
 */
public interface IMachineCompanyService extends IService<MachineCompany> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<MachineCompany> getPageList(MachineCompanyListDto pageListDto);
	String addMachineCompany(MachineCompany machineCompany);
	MachineCompany getMaxComCodeByPid(String id);
	List getListByName(String name);

	boolean updateMachineCompany(String id, MachineCompany machineCompany);
}
