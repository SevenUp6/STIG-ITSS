package com.xjrsoft.module.itss.repairGuide.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.repairGuide.entity.RepairGuide;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.module.itss.repairGuide.entity.XjrBaseAnnexesfile;
import com.xjrsoft.module.itss.repairGuide.mapper.XjrBaseAnnexesfileMapper;
import com.xjrsoft.module.itss.repairGuide.dto.RepairGuideListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;

import java.time.LocalDateTime;
import java.util.List;
import com.xjrsoft.module.itss.repairGuide.mapper.RepairGuideMapper;
import com.xjrsoft.module.itss.repairGuide.service.IRepairGuideService;
import com.xjrsoft.core.mp.base.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 维修指南表 服务实现类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Service
@AllArgsConstructor
public class RepairGuideServiceImpl extends BaseService<RepairGuideMapper, RepairGuide> implements IRepairGuideService {

	private XjrBaseAnnexesfileMapper xjrBaseAnnexesfileMapper;

	@Override
	public IPage<RepairGuide> getPageList(RepairGuideListDto pageListDto) {
		Wrapper<RepairGuide> wrapper = Wrappers.<RepairGuide>query().lambda()				.like(!StringUtil.isEmpty(pageListDto.getGuide_name()), RepairGuide::getGuideName, pageListDto.getGuide_name())
				.like(!StringUtil.isEmpty(pageListDto.getMod_name()), RepairGuide::getModName, pageListDto.getMod_name())
				.like(!StringUtil.isEmpty(pageListDto.getMod_id()), RepairGuide::getModId, pageListDto.getMod_id())
				.like(!StringUtil.isEmpty(pageListDto.getFault_name()), RepairGuide::getFaultName, pageListDto.getFault_name());
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public boolean addRepairGuide(RepairGuide repairGuide, List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList) {
		String repairGuideId = IdWorker.get32UUID();
		repairGuide.setId(repairGuideId);
		xjrBaseAnnexesfileList.forEach(xjrBaseAnnexesfile -> xjrBaseAnnexesfile.setFFolderid(repairGuideId));
		this.saveBatch(xjrBaseAnnexesfileList, XjrBaseAnnexesfile.class, XjrBaseAnnexesfileMapper.class);
		boolean isSuccess = this.save(repairGuide);
		return isSuccess;
	}
	@Override
	public String addRepairGuide(RepairGuide repairGuide ) {
		String userId = SecureUtil.getUserId();
		repairGuide.setCreatedBy(userId);
		repairGuide.setCreatedName(SecureUtil.getUserName());;
		repairGuide.setCreatedTime(LocalDateTime.now());
		String repairGuideId = IdWorker.get32UUID();
		repairGuide.setId(repairGuideId);
		boolean isSuccess = this.save(repairGuide);
		if(isSuccess){
			return repairGuideId;
		}else {
			return "error";
		}
	}

	@Override
	public boolean updateRepairGuide(String id, RepairGuide repairGuide, List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList) {
		repairGuide.setId(id);
//		xjrBaseAnnexesfileList.forEach(xjrBaseAnnexesfile -> xjrBaseAnnexesfile.setFFolderid(id));
//		xjrBaseAnnexesfileMapper.delete(Wrappers.<XjrBaseAnnexesfile>query().lambda().eq(XjrBaseAnnexesfile::getFFolderid, id));
//		this.saveBatch(xjrBaseAnnexesfileList, XjrBaseAnnexesfile.class, XjrBaseAnnexesfileMapper.class);
		return this.updateById(repairGuide);
	}

	public List<XjrBaseAnnexesfile> getXjrBaseAnnexesfileByParentId(String parentId){
		Wrapper wrapper = Wrappers.<XjrBaseAnnexesfile>query().lambda().eq(XjrBaseAnnexesfile::getFFolderid, parentId);
		return xjrBaseAnnexesfileMapper.selectList(wrapper);
	}
}
