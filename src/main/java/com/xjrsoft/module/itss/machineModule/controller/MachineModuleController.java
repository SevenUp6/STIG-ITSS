package com.xjrsoft.module.itss.machineModule.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.page.ConventPage;
import java.util.List;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.itss.machineModule.dto.MachineModuleDto;
import com.xjrsoft.module.itss.machineModule.dto.SaveMachineModuleFormDataDto;
import com.xjrsoft.module.itss.machineModule.vo.MachineModuleFormDataVo;
import com.xjrsoft.module.itss.machineModule.vo.MachineModuleListVo;
import com.xjrsoft.module.itss.machineModule.dto.MachineModuleListDto;
import com.xjrsoft.module.itss.machineModule.vo.FaultTypeVo;
import com.xjrsoft.module.itss.machineModule.entity.FaultType;
import com.xjrsoft.module.itss.machineModule.dto.FaultTypeDto;
import com.xjrsoft.module.itss.machineType.vo.MachineTypeFormDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import com.xjrsoft.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import com.xjrsoft.module.itss.machineModule.vo.MachineModuleVo;
import com.xjrsoft.module.itss.machineModule.service.IMachineModuleService;

/**
 * 设备模块表 控制器
 *
 * @author hanhe
 * @since 2022-10-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinemodule")
@Api(value = "设备模块表", tags = "设备模块表接口")
public class MachineModuleController {


	private final IMachineModuleService machineModuleService;


	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入machineModule")
	public Response<MachineModuleFormDataVo> getFormData(@PathVariable String id) {
		MachineModuleFormDataVo formDataVo = new MachineModuleFormDataVo();
		// 主表数据
		MachineModule machineModule = machineModuleService.getById(id);
		formDataVo.setMachineModuleVo(BeanUtil.copy(machineModule, MachineModuleVo.class));
		// 从表数据
		formDataVo.setFaultTypeVoList(BeanUtil.copyList(machineModuleService.getFaultTypeByParentId(id), FaultTypeVo.class));
		return Response.ok(formDataVo);
	}

	/**
	 * 自定义分页 设备模块表
	 */
	@GetMapping
	@ApiOperation(value = "分页", notes = "传入machineModule")
	public Response<PageOutput<MachineModuleListVo>> getPageList(MachineModuleListDto listDto) {
		IPage<MachineModule> page = machineModuleService.getPageList(listDto);
		List<MachineModuleListVo> records = BeanUtil.copyList(page.getRecords(), MachineModuleListVo.class);
		DataTransUtil.transListShowData(records);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 新增 设备模块表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入machineModule")
	public Response save(@RequestBody SaveMachineModuleFormDataDto formDto) {
		MachineModuleDto machineModuleDto = formDto.getMachineModuleDto();
		MachineModule machineModule = BeanUtil.copy(machineModuleDto, MachineModule.class);
//		List<FaultTypeDto> faultTypeDtoList = formDto.getFaultTypeDto();
//		List<FaultType> faultTypeList = BeanUtil.copyList(faultTypeDtoList, FaultType.class);
		List typelist=machineModuleService.getListByNamePid(machineModule.getTypeId(),machineModule.getModName());
		if(null !=typelist && typelist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+"】");
		}
		boolean isSuccess = machineModuleService.addMachineModule(machineModule, null);
		return Response.status(isSuccess);
	}

	/**
	 * 修改 设备模块表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入machineModule")
	public Response update(@PathVariable String id, @RequestBody SaveMachineModuleFormDataDto formDto) {
		MachineModuleDto machineModuleDto = formDto.getMachineModuleDto();
		MachineModule machineModule = BeanUtil.copy(machineModuleDto, MachineModule.class);
		List typelist=machineModuleService.getListByNamePid(machineModule.getTypeId(),machineModule.getModName());
		if(null !=typelist && typelist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+"】");
		}
		return Response.status(machineModuleService.updateMachineModule(id, machineModule, null));
	}


	
	/**
	 * 删除 设备模块表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		List faulttype=machineModuleService.getFaultTypeByParentId(ids);
		if(null != faulttype && faulttype.size()>0){
			return Response.notOk(1001,"该设备模块下绑定了故障类型，不能删除！");
		}
		return Response.status(machineModuleService.removeByIds(Func.toStrList(ids)));
	}

	@GetMapping("/getFault/{id}")
	@ApiOperation(value = "根据id获取模块", notes = "传入moudleid")
	public Response<MachineModuleFormDataVo> getMoudleById(@PathVariable String id) {
		MachineModuleFormDataVo formDataVo = new MachineModuleFormDataVo();
		formDataVo.setFaultTypeVoList(BeanUtil.copyList(machineModuleService.getFaultTypeByParentId(id), FaultTypeVo.class));
		return Response.ok(formDataVo);
	}
}
