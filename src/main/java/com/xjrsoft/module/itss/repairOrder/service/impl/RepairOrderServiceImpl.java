package com.xjrsoft.module.itss.repairOrder.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.core.mp.base.BaseService;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.service.IXjrBaseRoleService;
import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderDto;
import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderListDto;
import com.xjrsoft.module.itss.repairOrder.entity.RepairOrder;
import com.xjrsoft.module.itss.repairOrder.mapper.RepairOrderMapper;
import com.xjrsoft.module.itss.repairOrder.service.IRepairOrderService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 维修工单表 服务实现类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Service
@AllArgsConstructor
public class RepairOrderServiceImpl extends BaseService<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

	private  IXjrBaseRoleService roleService;

	@Override
	public IPage<RepairOrder> getPageList(RepairOrderListDto pageListDto) {
		Wrapper<RepairOrder> wrapper;
		Boolean isAdminRole=isAdminRole(pageListDto.getCreated_by());
		if(isAdminRole){
			wrapper = Wrappers.<RepairOrder>query().lambda().eq(!StringUtil.isEmpty(pageListDto.getIsurgent()), RepairOrder::getIsurgent, pageListDto.getIsurgent())
					.eq(!StringUtil.isEmpty(pageListDto.getCode()), RepairOrder::getCode, pageListDto.getCode())
					.eq(!StringUtil.isEmpty(pageListDto.getStatus()), RepairOrder::getStatus, pageListDto.getStatus())
					.like(!StringUtil.isEmpty(pageListDto.getReport_name()), RepairOrder::getReportName, pageListDto.getReport_name())
					.like(!StringUtil.isEmpty(pageListDto.getReport_phone()), RepairOrder::getReportPhone, pageListDto.getReport_phone())
					.like(!StringUtil.isEmpty(pageListDto.getCreated_time()), RepairOrder::getCreatedTime, pageListDto.getCreated_time())
					.like(!StringUtil.isEmpty(pageListDto.getRepair_usrname()), RepairOrder::getRepairUsrname, pageListDto.getRepair_usrname())
					.ge(!StringUtil.isEmpty(pageListDto.getAssign_time_Start()), RepairOrder::getAssignTime, pageListDto.getAssign_time_Start())
					.le(!StringUtil.isEmpty(pageListDto.getAssign_time_End()), RepairOrder::getAssignTime, pageListDto.getAssign_time_End())
					.like(!StringUtil.isEmpty(pageListDto.getRepair_time()), RepairOrder::getRepairTime, pageListDto.getRepair_time())
					.like(!StringUtil.isEmpty(pageListDto.getType_name()), RepairOrder::getTypeName, pageListDto.getType_name())
					.like(!StringUtil.isEmpty(pageListDto.getMod_name()), RepairOrder::getModName, pageListDto.getMod_name())
					.like(!StringUtil.isEmpty(pageListDto.getFau_name()), RepairOrder::getFauName, pageListDto.getFau_name())
					.eq(!StringUtil.isEmpty(pageListDto.getHandle_type()), RepairOrder::getHandleType, pageListDto.getHandle_type())
					.eq(!StringUtil.isEmpty(pageListDto.getReason()), RepairOrder::getReason, pageListDto.getReason()).orderByAsc(RepairOrder::getStatus).orderByDesc(RepairOrder::getIsurgent,RepairOrder::getAssignTime);

		}else{
			wrapper = Wrappers.<RepairOrder>query().lambda().eq(!StringUtil.isEmpty(pageListDto.getIsurgent()), RepairOrder::getIsurgent, pageListDto.getIsurgent())
					.eq(!StringUtil.isEmpty(pageListDto.getCode()), RepairOrder::getCode, pageListDto.getCode())
					.eq(!StringUtil.isEmpty(pageListDto.getStatus()), RepairOrder::getStatus, pageListDto.getStatus())
					.like(!StringUtil.isEmpty(pageListDto.getReport_name()), RepairOrder::getReportName, pageListDto.getReport_name())
					.like(!StringUtil.isEmpty(pageListDto.getReport_phone()), RepairOrder::getReportPhone, pageListDto.getReport_phone())
					.like(!StringUtil.isEmpty(pageListDto.getCreated_time()), RepairOrder::getCreatedTime, pageListDto.getCreated_time())
					.like(!StringUtil.isEmpty(pageListDto.getRepair_usrname()), RepairOrder::getRepairUsrname, pageListDto.getRepair_usrname())
					.ge(!StringUtil.isEmpty(pageListDto.getAssign_time_Start()), RepairOrder::getAssignTime, pageListDto.getAssign_time_Start())
					.le(!StringUtil.isEmpty(pageListDto.getAssign_time_End()), RepairOrder::getAssignTime, pageListDto.getAssign_time_End())
					.like(!StringUtil.isEmpty(pageListDto.getRepair_time()), RepairOrder::getRepairTime, pageListDto.getRepair_time())
					.like(!StringUtil.isEmpty(pageListDto.getType_name()), RepairOrder::getTypeName, pageListDto.getType_name())
					.like(!StringUtil.isEmpty(pageListDto.getMod_name()), RepairOrder::getModName, pageListDto.getMod_name())
					.like(!StringUtil.isEmpty(pageListDto.getFau_name()), RepairOrder::getFauName, pageListDto.getFau_name())
					.eq(!StringUtil.isEmpty(pageListDto.getHandle_type()), RepairOrder::getHandleType, pageListDto.getHandle_type())
					.and(w->w.eq(!StringUtil.isEmpty(pageListDto.getCreated_by()), RepairOrder::getCreatedBy, pageListDto.getCreated_by())
							.or().eq(!StringUtil.isEmpty(pageListDto.getRepair_usrid()), RepairOrder::getRepairUsrid, pageListDto.getRepair_usrid())
							.apply("1=1")		).eq(!StringUtil.isEmpty(pageListDto.getReason()), RepairOrder::getReason, pageListDto.getReason()).orderByAsc(RepairOrder::getStatus).orderByDesc(RepairOrder::getIsurgent,RepairOrder::getAssignTime);

		}
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public IPage<RepairOrder> getDataList(RepairOrderDto repairOrderDto) {
		Wrapper<RepairOrder> wrapper = Wrappers.<RepairOrder>query().lambda()				.eq(!StringUtil.isEmpty(repairOrderDto.getIsurgent()), RepairOrder::getIsurgent, repairOrderDto.getIsurgent())
				.eq(!StringUtil.isEmpty(repairOrderDto.getStatus()), RepairOrder::getStatus, repairOrderDto.getStatus())
				.like(!StringUtil.isEmpty(repairOrderDto.getReportName()), RepairOrder::getReportName, repairOrderDto.getReportName())
				.like(!StringUtil.isEmpty(repairOrderDto.getReportPhone()), RepairOrder::getReportPhone, repairOrderDto.getReportPhone())
				.like(!StringUtil.isEmpty(repairOrderDto.getCreatedTime()), RepairOrder::getCreatedTime, repairOrderDto.getCreatedTime())
				.like(!StringUtil.isEmpty(repairOrderDto.getRepairUsrname()), RepairOrder::getRepairUsrname, repairOrderDto.getRepairUsrname())
				.ge(!StringUtil.isEmpty(repairOrderDto.getAssign_time_Start()), RepairOrder::getAssignTime, repairOrderDto.getAssign_time_Start())
				.le(!StringUtil.isEmpty(repairOrderDto.getAssign_time_End()), RepairOrder::getAssignTime, repairOrderDto.getAssign_time_End())
				.like(!StringUtil.isEmpty(repairOrderDto.getRepairTime()), RepairOrder::getRepairTime, repairOrderDto.getRepairTime())
				.like(!StringUtil.isEmpty(repairOrderDto.getTypeName()), RepairOrder::getTypeName, repairOrderDto.getTypeName())
				.like(!StringUtil.isEmpty(repairOrderDto.getModName()), RepairOrder::getModName, repairOrderDto.getModName())
				.like(!StringUtil.isEmpty(repairOrderDto.getFauName()), RepairOrder::getFauName, repairOrderDto.getFauName())
				.eq(!StringUtil.isEmpty(repairOrderDto.getHandleType()), RepairOrder::getHandleType, repairOrderDto.getHandleType())
				.and(w->w.eq(!StringUtil.isEmpty(repairOrderDto.getCreatedBy()), RepairOrder::getCreatedBy, repairOrderDto.getCreatedBy())
						.or().eq(!StringUtil.isEmpty(repairOrderDto.getRepairUsrid()), RepairOrder::getRepairUsrid, repairOrderDto.getRepairUsrid())
				).eq(!StringUtil.isEmpty(repairOrderDto.getReason()), RepairOrder::getReason, repairOrderDto.getReason()).orderByDesc(RepairOrder::getIsurgent,RepairOrder::getAssignTime);
		return this.page(ConventPage.getPage(repairOrderDto), wrapper);
	}

	@Override
	public boolean addRepairOrder(RepairOrder repairOrder) {
		boolean isSuccess = this.save(repairOrder);
		return isSuccess;
	}

	@Override
	public boolean updateRepairOrder(String id, RepairOrder repairOrder) {
		repairOrder.setId(id);
		return this.updateById(repairOrder);
	}

	@Override
	public boolean updateRepairUser(String id ,String userid,String username) {
		UpdateWrapper<RepairOrder> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("repair_usrid",userid );
		updateWrapper.set("repair_usrname",username );
		updateWrapper.set("assign_time", LocalDateTime.now());
		updateWrapper.eq("id", id);
		return this.update(updateWrapper);
	}

	@Override
	public boolean updateStatusById(String id,int status,String fauid,String fauname ,
									String resultdes,String machine_sn,String handle_type,String reason){
		UpdateWrapper<RepairOrder> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("status",status );
		if(!StringUtil.isEmpty(fauid)&&!StringUtil.isEmpty(fauname)){
			updateWrapper.set("fau_id",fauid );
			updateWrapper.set("fau_name",fauname );
		}
		if(9==status){
			updateWrapper.set("repair_time", LocalDateTime.now());
			updateWrapper.set("result_des", resultdes);
			updateWrapper.set("machine_sn", machine_sn);
			updateWrapper.set("handle_type", Integer.parseInt(handle_type));
			updateWrapper.set("reason", Integer.parseInt(reason));
		}else {
			updateWrapper.set("updated_time", LocalDateTime.now());
		}
		updateWrapper.eq("id", id);
		return this.update(updateWrapper);
	}


	public  Boolean isAdminRole(String userId){
		boolean isAdminRole = false;
		String currentUserId = SecureUtil.getUserId();
		XjrBaseRole sysRole = roleService.getSysRole();
		List<XjrBaseRole> rolesOfUserId = roleService.getRolesByUserId(currentUserId);
		// 判断权限
		for (XjrBaseRole xjrBaseRole : rolesOfUserId) {
			if (StringUtils.equals(xjrBaseRole.getRoleId(), sysRole.getRoleId())) {
				isAdminRole = true;
				break;
			}
		}
		return isAdminRole;
	}
}
