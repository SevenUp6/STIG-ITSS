package com.xjrsoft.module.itss.faultType.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.page.ConventPage;
import java.util.List;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeDto;
import com.xjrsoft.module.itss.faultType.dto.SaveFaultTypeFormDataDto;
import com.xjrsoft.module.itss.faultType.vo.FaultTypeVo;
import com.xjrsoft.module.itss.faultType.vo.FaultTypeListVo;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import com.xjrsoft.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.xjrsoft.module.itss.faultType.entity.FaultType;
import com.xjrsoft.module.itss.faultType.vo.FaultTypeVo;
import com.xjrsoft.module.itss.faultType.service.IFaultTypeService;

/**
 * 故障类型表 控制器
 *
 * @author hanhe
 * @since 2022-10-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/faulttype")
@Api(value = "故障类型表", tags = "故障类型表接口")
public class FaultTypeController {


	private final IFaultTypeService faultTypeService;


	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入faultType")
	public Response<FaultTypeVo> getFormData(@PathVariable String id) {
		// 主表数据
		FaultType faultType = faultTypeService.getById(id);
		return Response.ok(BeanUtil.copy(faultType, FaultTypeVo.class));
	}

	/**
	 * 自定义分页 故障类型表
	 */
	@GetMapping
	@ApiOperation(value = "分页", notes = "传入faultType")
	public Response<PageOutput<FaultTypeListVo>> getPageList(FaultTypeListDto listDto) {
		IPage<FaultType> page = faultTypeService.getPageList(listDto);
		List<FaultTypeListVo> records = BeanUtil.copyList(page.getRecords(), FaultTypeListVo.class);
		DataTransUtil.transListShowData(records);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 新增 故障类型表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入faultType")
	public Response save(@RequestBody SaveFaultTypeFormDataDto formDto) {
		FaultTypeDto faultTypeDto = formDto.getFaultTypeDto();
		FaultType faultType = BeanUtil.copy(faultTypeDto, FaultType.class);
		boolean isSuccess = faultTypeService.addFaultType(faultType);
		List typelist=faultTypeService.getListByNamePid(faultType.getModId(),faultType.getFauName());
		if(null !=typelist && typelist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+"】");
		}
		return Response.status(isSuccess);
	}

	/**
	 * 修改 故障类型表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入faultType")
	public Response update(@PathVariable String id, @RequestBody SaveFaultTypeFormDataDto formDto) {
		FaultTypeDto faultTypeDto = formDto.getFaultTypeDto();
		FaultType faultType = BeanUtil.copy(faultTypeDto, FaultType.class);
		return Response.status(faultTypeService.updateFaultType(id, faultType));
	}


	
	/**
	 * 删除 故障类型表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		return Response.status(faultTypeService.removeByIds(Func.toStrList(ids)));
	}

	
}
