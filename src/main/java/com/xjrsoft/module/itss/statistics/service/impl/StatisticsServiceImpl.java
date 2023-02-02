package com.xjrsoft.module.itss.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.core.mp.base.BaseService;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.repairOrder.entity.RepairOrder;
import com.xjrsoft.module.itss.statistics.dto.GdmxDto;
import com.xjrsoft.module.itss.statistics.entity.MachineType;
import com.xjrsoft.module.itss.statistics.mapper.StatisticsMapper;
import com.xjrsoft.module.itss.statistics.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备种类表 服务实现类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Service
@AllArgsConstructor
public class StatisticsServiceImpl  implements StatisticsService {

private StatisticsMapper statisticsMapper;
	public List  getSbgzData(String type_id, String start,String end){

		return statisticsMapper.getSbgzData(type_id,start,end);
	}
	public List  getGzlxData(String type_id, String mod_id,String start,String end){

		return statisticsMapper.getGzlxData(type_id,mod_id,start,end);
	}
	public List getGdclData_0(String type_id, String start,String end){

		return statisticsMapper.getGdclData_0(type_id,start,end);
	}
	public List getClsxData(String type_id, String start,String end){

		return statisticsMapper.getClsxData(type_id,start,end);
	}

	public List getGdslData(){

		return statisticsMapper.getGdslData();
	}
//	public IPage<RepairOrder> getGdmxData(GdmxDto gdmxDto){

//		Wrapper<RepairOrder> wrapper = Wrappers.<RepairOrder>query().lambda()				.eq(!StringUtil.isEmpty(gdmxDto.getIsurgent()), RepairOrder::getIsurgent, gdmxDto.getIsurgent())
//				.eq(!StringUtil.isEmpty(gdmxDto.getStatus()), RepairOrder::getStatus, gdmxDto.getStatus())
//				.like(!StringUtil.isEmpty(gdmxDto.getReportName()), RepairOrder::getReportName, gdmxDto.getReportName())
//				.like(!StringUtil.isEmpty(gdmxDto.getReportPhone()), RepairOrder::getReportPhone, gdmxDto.getReportPhone())
//				.like(!StringUtil.isEmpty(gdmxDto.getCreatedBy()), RepairOrder::getCreatedBy, gdmxDto.getCreatedBy())
//				.like(!StringUtil.isEmpty(gdmxDto.getRepairUsrid()), RepairOrder::getRepairUsrid, gdmxDto.getRepairUsrid())
//				.eq(!StringUtil.isEmpty(gdmxDto.getCreatedTime()), RepairOrder::getCreatedTime, gdmxDto.getCreatedTime())
//				.like(!StringUtil.isEmpty(gdmxDto.getRepairUsrname()), RepairOrder::getRepairUsrname, gdmxDto.getRepairUsrname())
//				.ge(!StringUtil.isEmpty(gdmxDto.getAssignTime()), RepairOrder::getAssignTime, gdmxDto.getAssignTime())
//				.le(!StringUtil.isEmpty(gdmxDto.getAssignTime()), RepairOrder::getAssignTime, gdmxDto.getAssignTime())
//				.eq(!StringUtil.isEmpty(gdmxDto.getRepairTime()), RepairOrder::getRepairTime, gdmxDto.getRepairTime())
//				.like(!StringUtil.isEmpty(gdmxDto.getTypeName()), RepairOrder::getTypeName, gdmxDto.getTypeName())
//				.like(!StringUtil.isEmpty(gdmxDto.getModName()), RepairOrder::getModName, gdmxDto.getModName())
//				.like(!StringUtil.isEmpty(gdmxDto.getFauName()), RepairOrder::getFauName, gdmxDto.getFauName())
//				.eq(!StringUtil.isEmpty(gdmxDto.getHandleType()), RepairOrder::getHandleType, gdmxDto.getHandleType())
//				.eq(!StringUtil.isEmpty(gdmxDto.getReason()), RepairOrder::getReason, gdmxDto.getReason());
//		return this.page(ConventPage.getPage(gdmxDto), wrapper);
//	}


}
