package com.xjrsoft.module.itss.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.AjaxResult;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.common.utils.ExcelUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderListDto;
import com.xjrsoft.module.itss.repairOrder.entity.RepairOrder;
import com.xjrsoft.module.itss.repairOrder.service.IRepairOrderService;
import com.xjrsoft.module.itss.statistics.service.StatisticsService;
import com.xjrsoft.module.itss.statistics.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 设备种类表 控制器
 *
 * @author HANHE
 * @since 2022-10-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statistics")
@Api(value = "统计报表类", tags = "统计报表接口")
public class StatisticsController {


	private final StatisticsService statisticsService;
	@Autowired
	private final IRepairOrderService repairOrderService;

	/**
	 * 设备故障统计
	 */
	@GetMapping("/sbgz-data")
	@ApiOperation(value = "根据传入条件获取设备故障统计报表", notes = "传入设备种类，维修时间")
	public String  getSbgzData(@RequestParam(required=false) String type_id,@RequestParam(required=false) String start,@RequestParam(required=false) String end) {
		List<StatisticsSbgzVo> SbgzData =statisticsService.getSbgzData(type_id,start,end);
		String jsonString = JSON.toJSONString(SbgzData);
		System.out.println(jsonString);
		return  jsonString;
	}
	/**
	 * 故障类型统计
	 */
	@GetMapping("/gzlx-data")
	@ApiOperation(value = "根据传入条件获取故障类型统计报表", notes = "传入设备种类，设备模块,维修时间")
	public String  getGzlxData(@RequestParam(required=false) String type_id,@RequestParam(required=false) String mod_id,@RequestParam(required=false) String start,@RequestParam(required=false) String end) {
		List<StatisticsGzlxVo> SbgzData =statisticsService.getGzlxData(type_id,mod_id,start,end);
		String jsonString = JSON.toJSONString(SbgzData);
		System.out.println(jsonString);
		return  jsonString;
	}

	/**
	 * 工单处理统计
	 */
	@GetMapping("/gdcl-data")
	@ApiOperation(value = "根据传入条件获取工单处理统计报表", notes = "传入设备种类，设备模块,维修时间")
	public String  getGdclData(@RequestParam(required=false) String type_id,@RequestParam(required=false) String start) {
		List <StatisticsGdlx0Vo>SbgzData_0 =statisticsService.getGdclData_0(type_id,start,null);
		String jsonString = JSON.toJSONString(SbgzData_0);
		System.out.println(jsonString);
		return  jsonString;
	}

	/**
	 * 处理时效统计
	 */
	@GetMapping("/clsx-data")
	@ApiOperation(value = "根据传入条件获取工单处理统计报表", notes = "传入设备种类，设备模块,维修时间")
	public String  getClsxData(@RequestParam(required=false) String type_id,@RequestParam(required=false) String start) {
		List <StatisticsClsxVo>ClsxData =statisticsService.getClsxData(type_id,start,null);
		String jsonString = JSON.toJSONString(ClsxData);
		System.out.println(jsonString);
		return  jsonString;
	}
	/**
	 * 工单明细统计
	 */
	@GetMapping("/gdmx-data")
	@ApiOperation(value = "根据传入条件获取工单处理统计报表", notes = "传入设备种类，设备模块,维修时间")
	public Response<PageOutput<StatisticsGdmxVo>> getGdmxData(RepairOrderListDto listDto) {
		IPage<RepairOrder> page = repairOrderService.getPageList(listDto);
		List<StatisticsGdmxVo> records = BeanUtil.copyList(page.getRecords(), StatisticsGdmxVo.class);
		// 转换列表数据
		DataTransUtil.transListShowData(records);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 第二天统计前一天各设备种类新增工单数量
	 */
	@GetMapping("/gdsl-data")
	@ApiOperation(value = "定时统计前一天各设备种类新增工单数量", notes = "传入设备种类，设备模块,维修时间")
	public String getGdslData() {
		List <StatisticsGdslVo>GdslData =statisticsService.getGdslData();
		String yesterday=   LocalDate.now().minusDays(1).toString();
		String jsonString = JSON.toJSONString(GdslData);
		StringBuilder sb=new StringBuilder("{\"text\":"+"\""+"各位领导上午好， ")
				.append(yesterday).append("  各运维系统及设备故障维修情况如下：");
//		("{\"text\":"+"\""+"给位领导上午好，"+year+"各运维系统及设备故障维修情况如下：\",\"title\": \"昨日维修情况\"}");
		int i=0;
		for (StatisticsGdslVo statisticsGdslVo:GdslData){
			i++;
			sb.append(i).append(".").append(statisticsGdslVo.getTypeName()).append("：新增的总工单：").append(statisticsGdslVo.getSum()).append("条；已处理：").append(statisticsGdslVo.getSum9()).append(";未处理").append(statisticsGdslVo.getSum0()).append("。");
		}
		sb.append("\",\"title\": \"昨日维修工单情况\"}");
		return  sb.toString();
	}

//-------------------------------------导出部分----------------------------------------------------------
@GetMapping("/sbgz-export")
@ApiOperation(value = "设备故障统计导出", notes = "传入设备种类，维修时间")
public void export(HttpServletResponse response,@RequestParam(required=false) String type_id, @RequestParam(required=false) String start, @RequestParam(required=false) String end) {
	List<StatisticsSbgzVo> SbgzData =statisticsService.getSbgzData(type_id,start,end);
	ExcelUtil<StatisticsSbgzVo> util = new ExcelUtil<>(StatisticsSbgzVo.class);
	Workbook workbook = null;
	workbook = util.exportExcelWithoutPersist(SbgzData, "设备故障统计");
	response.setContentType("application/octet-stream");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	response.setHeader("Content-disposition", String.format("attachment;filename=sbgz-export_%s.xlsx", sdf.format(new Date())));
	try {
		response.flushBuffer();
		workbook.write(response.getOutputStream());
	} catch (IOException e) {
		throw new RuntimeException(e);
	}
}

	@GetMapping("/gzlx-export")
	@ApiOperation(value = "故障类型统计导出", notes = "传入设备种类，维修时间")
	public void gzlxexport(HttpServletResponse response,@RequestParam(required=false) String type_id,@RequestParam(required=false) String mod_id,@RequestParam(required=false) String start,@RequestParam(required=false) String end) {
		List<StatisticsGzlxVo> SbgzData =statisticsService.getGzlxData(type_id,mod_id,start,end);
		ExcelUtil<StatisticsGzlxVo> util = new ExcelUtil<>(StatisticsGzlxVo.class);
		Workbook workbook = null;
		workbook = util.exportExcelWithoutPersist(SbgzData, "故障类型统计");
		response.setContentType("application/octet-stream");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		response.setHeader("Content-disposition", String.format("attachment;filename=gzlx-export_%s.xlsx", sdf.format(new Date())));
		try {
			response.flushBuffer();
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/gdcl-export")
	@ApiOperation(value = "工单处理统计导出", notes = "传入设备种类，维修时间")
	public void gdclexport(HttpServletResponse response,@RequestParam(required=false) String type_id,@RequestParam(required=false) String start) {
		List <StatisticsGdlx0Vo>SbgzData_0 =statisticsService.getGdclData_0(type_id,start,null);
		ExcelUtil<StatisticsGdlx0Vo> util = new ExcelUtil<>(StatisticsGdlx0Vo.class);
		Workbook workbook = null;
		workbook = util.exportExcelWithoutPersist(SbgzData_0, "工单处理统计");
		response.setContentType("application/octet-stream");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		response.setHeader("Content-disposition", String.format("attachment;filename=gdcl-export_%s.xlsx", sdf.format(new Date())));
		try {
			response.flushBuffer();
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/clsx-export")
	@ApiOperation(value = "处理时效统计导出", notes = "传入设备种类，维修时间")
	public void clsxExport(HttpServletResponse response,@RequestParam(required=false) String type_id,@RequestParam(required=false) String start) {
		List <StatisticsClsxVo>ClsxData =statisticsService.getClsxData(type_id,start,null);
		ExcelUtil<StatisticsClsxVo> util = new ExcelUtil<>(StatisticsClsxVo.class);
		Workbook workbook = null;
		workbook = util.exportExcelWithoutPersist(ClsxData, "处理时效统计");
		response.setContentType("application/octet-stream");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		response.setHeader("Content-disposition", String.format("attachment;filename=clsx-export_%s.xlsx", sdf.format(new Date())));
		try {
			response.flushBuffer();
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/gdmx-export")
	@ApiOperation(value = "工单明细统计导出", notes = "传入设备种类，维修时间")
	public void clsxExport(HttpServletResponse response,RepairOrderListDto listDto) {
		IPage<RepairOrder> page = repairOrderService.getPageList(listDto);
		List<StatisticsGdmxVo> records = BeanUtil.copyList(page.getRecords(), StatisticsGdmxVo.class);
		ExcelUtil<StatisticsGdmxVo> util = new ExcelUtil<>(StatisticsGdmxVo.class);
		Workbook workbook = null;
		workbook = util.exportExcelWithoutPersist(records, "工单明细统计");
		response.setContentType("application/octet-stream");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		response.setHeader("Content-disposition", String.format("attachment;filename=gdmx-export_%s.xlsx", sdf.format(new Date())));
		try {
			response.flushBuffer();
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
