package com.xjrsoft.module.itss.repairOrder.service;

import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderDto;
import com.xjrsoft.module.itss.repairOrder.entity.RepairOrder;
import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 维修工单表 服务类
 *
 * @author hanhe
 * @since 2022-10-13
 */
public interface IRepairOrderService extends IService<RepairOrder> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<RepairOrder> getPageList(RepairOrderListDto pageListDto);
	IPage<RepairOrder> getPageList4Statistics(RepairOrderListDto pageListDto);

	IPage<RepairOrder> getDataList(RepairOrderDto repairOrderDto);
	boolean addRepairOrder(RepairOrder repairOrder);

	boolean updateRepairOrder(String id, RepairOrder repairOrder);
	boolean updateRepairUser(String id ,String userid,String username) ;

	boolean updateStatusById(String id,int status ,String fauid,String fauname,String result_des,String machine_sn,String handle_type,String reason) ;
}
