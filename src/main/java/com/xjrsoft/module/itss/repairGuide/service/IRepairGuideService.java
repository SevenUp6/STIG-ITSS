package com.xjrsoft.module.itss.repairGuide.service;

import com.xjrsoft.module.itss.repairGuide.entity.RepairGuide;
import com.xjrsoft.module.itss.repairGuide.entity.XjrBaseAnnexesfile;
import com.xjrsoft.module.itss.repairGuide.dto.RepairGuideListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 维修指南表 服务类
 *
 * @author hanhe
 * @since 2022-10-20
 */
public interface IRepairGuideService extends IService<RepairGuide> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<RepairGuide> getPageList(RepairGuideListDto pageListDto);

	List<XjrBaseAnnexesfile> getXjrBaseAnnexesfileByParentId(String parentId);
	boolean addRepairGuide(RepairGuide repairGuide, List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList);
	String addRepairGuide(RepairGuide repairGuide);

	boolean updateRepairGuide(String id, RepairGuide repairGuide, List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList);
}
