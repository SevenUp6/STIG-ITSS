package com.xjrsoft.module.itss.statistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.itss.statistics.dto.StatisticsGdmxListDto;
import com.xjrsoft.module.itss.statistics.dto.StatisticsListDto;
import com.xjrsoft.module.itss.statistics.entity.FaultType;
import com.xjrsoft.module.itss.statistics.entity.MachineModule;
import com.xjrsoft.module.itss.statistics.entity.MachineType;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 设备种类表 服务类
 *
 * @author HANHE
 * @since 2022-10-12
 */
public interface StatisticsService  {

	List  getSbgzData(String type_id, String start,String end);

	List  getGzlxData(String type_id, String mod_id,String start,String end);
	List  getGdclData_0(String type_id, String start,String end);
	List  getClsxData(String type_id, String start,String end);
	List  getGdslData();
//	List  getGdmxData(StatisticsGdmxListDto gdmxListDto);

}
