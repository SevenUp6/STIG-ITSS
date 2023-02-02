package com.xjrsoft.module.itss.machineType.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeListDto;
import com.xjrsoft.module.itss.faultType.service.IFaultTypeService;
import com.xjrsoft.module.itss.machineModule.dto.MachineModuleListDto;
import com.xjrsoft.module.itss.machineModule.service.IMachineModuleService;
import com.xjrsoft.module.itss.machineModule.vo.MachineModuleVo;
import com.xjrsoft.module.itss.machineType.dto.*;
import com.xjrsoft.module.itss.machineType.entity.MachineType;
import com.xjrsoft.module.itss.machineType.service.IMachineTypeService;
import com.xjrsoft.module.itss.machineType.vo.FaultTypeVo;
import com.xjrsoft.module.itss.machineType.vo.MachineTypeFormDataVo;
import com.xjrsoft.module.itss.machineType.vo.MachineTypeListVo;
import com.xjrsoft.module.itss.machineType.vo.MachineTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备种类表 控制器
 *
 * @author HANHE
 * @since 2022-10-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinetype")
@Api(value = "设备种类表", tags = "设备种类表接口")
public class MachineTypeController {


	private final IMachineTypeService machineTypeService;

	private final IMachineModuleService machineModuleService;

	private final IFaultTypeService faultTypeService;
	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入machineType")
	public Response<MachineTypeFormDataVo> getFormData(@PathVariable String id) {
		MachineTypeFormDataVo formDataVo = new MachineTypeFormDataVo();
		// 主表数据
		MachineType machineType = machineTypeService.getById(id);
		formDataVo.setMachineTypeVo(BeanUtil.copy(machineType, MachineTypeVo.class));
		// 从表数据
		formDataVo.setFaultTypeVoList(BeanUtil.copyList(machineTypeService.getFaultTypeByParentId(id), FaultTypeVo.class));
		formDataVo.setMachineModuleVoList(BeanUtil.copyList(machineTypeService.getMachineModuleByParentId(id), MachineModuleVo.class));
		return Response.ok(formDataVo);
	}

	/**
	 * 自定义分页 设备种类表
	 */
	@GetMapping
	@ApiOperation(value = "分页", notes = "传入machineType")
	public Response<PageOutput<MachineTypeListVo>> getPageList(MachineTypeListDto listDto,@RequestParam(value = "mach_name",required = false) String mach_name) {
		listDto.setMach_name(mach_name);
		IPage<MachineType> page = machineTypeService.getPageList(listDto);
		List<MachineTypeListVo> records = BeanUtil.copyList(page.getRecords(), MachineTypeListVo.class);
		DataTransUtil.transListShowData(records);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 新增 设备种类表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入machineType")
	public Response save(@RequestBody SaveMachineTypeFormDataDto formDto) {
		MachineTypeDto machineTypeDto = formDto.getMachineTypeDto();
		MachineType machineType = BeanUtil.copy(machineTypeDto, MachineType.class);
//		List<FaultTypeDto> faultTypeDtoList = formDto.getFaultTypeDto();
//		List<FaultType> faultTypeList = BeanUtil.copyList(faultTypeDtoList, FaultType.class);
//		List<MachineModuleDto> machineModuleDtoList = formDto.getMachineModuleDto();
//		List<MachineModule> machineModuleList = BeanUtil.copyList(machineModuleDtoList, MachineModule.class);
		List typelist=machineTypeService.getListByName(machineType.getMachName());
		if(null !=typelist && typelist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的设备类型名称【"+"】");
		}
		boolean isSuccess = machineTypeService.addMachineType(machineType, null, null);
		return Response.status(isSuccess);
	}

	/**
	 * 修改 设备种类表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入machineType")
	public Response update(@PathVariable String id, @RequestBody SaveMachineTypeFormDataDto formDto) {
		MachineTypeDto machineTypeDto = formDto.getMachineTypeDto();
		MachineType machineType = BeanUtil.copy(machineTypeDto, MachineType.class);

//		List<FaultTypeDto> faultTypeDtoList = formDto.getFaultTypeDto();
//		List<FaultType> faultTypeList = BeanUtil.copyList(faultTypeDtoList, FaultType.class);
//
//		List<MachineModuleDto> machineModuleDtoList = formDto.getMachineModuleDto();
//		List<MachineModule> machineModuleList = BeanUtil.copyList(machineModuleDtoList, MachineModule.class);
		List typelist=machineTypeService.getListByName(machineType.getMachName());
		if(null !=typelist && typelist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的设备类型名称【"+"】");
		}
		return Response.status(machineTypeService.updateMachineType(id, machineType, null, null));
	}


	
	/**
	 * 删除 设备种类表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		List moudle =machineTypeService.getMachineModuleByParentId(ids);
		if(null !=moudle && moudle.size()>0){
			return Response.notOk(1001,"该设备种类下绑定了设备模块，不能删除！");
		}
		return Response.status(machineTypeService.removeByIds(Func.toStrList(ids)));
	}

	/**
	 * 获取全部数据，设备种类，设备模块，故障类型三级联动json
	 */
	@GetMapping("/getalldata")
	@ApiOperation(value = "获取全部数据，设备种类，设备模块，故障类型三级联动json", notes = "获取全部数据，设备种类，设备模块，故障类型三级联动json")
	public String  getAllData() {
		MachineTypeListDto listDto = new MachineTypeListDto();
		MachineModuleListDto listDto2 = new MachineModuleListDto();
		FaultTypeListDto listDto3 = new FaultTypeListDto();
		//设备种类数据
		IPage<MachineType> machineTypepage = machineTypeService.getPageList(listDto);
		List<MachineTypeDto> machineTypeData = BeanUtil.copyList(machineTypepage.getRecords(), MachineTypeDto.class);

		List<MachineTypeDto> machineTypeList = new ArrayList<>();
		for (MachineTypeDto mchineType : machineTypeData) {
			MachineTypeDto mchineType1 = new MachineTypeDto();
			mchineType1.setId(mchineType.getId());
			mchineType1.setMachName(mchineType.getMachName());
			//设备模块数据
			listDto2.setType_id(mchineType.getId());
			IPage<com.xjrsoft.module.itss.machineModule.entity.MachineModule> machineMoudlepage = machineModuleService.getPageList(listDto2);
			List<MachineModuleDto> MachineModuleData = BeanUtil.copyList(machineMoudlepage.getRecords(), MachineModuleDto.class);
			List<MachineModuleDto> machineModuleList = new ArrayList<>();
			for (MachineModuleDto machineModule : MachineModuleData) {
				MachineModuleDto machineModule1 = new MachineModuleDto();
				machineModule1.setId(machineModule.getId());
				machineModule1.setModName(machineModule.getModName());
				//设备故障数据
				listDto3.setMod_id(machineModule.getId());
				IPage<com.xjrsoft.module.itss.faultType.entity.FaultType> faultTypepage = faultTypeService.getPageList(listDto3);
				List<FaultTypeDto> faultTypeData = BeanUtil.copyList(faultTypepage.getRecords(), FaultTypeDto.class);
				machineModule1.setFaultTypes(faultTypeData);
				machineModuleList.add(machineModule1);
			}
			mchineType1.setMachineModules(machineModuleList);
			machineTypeList.add(mchineType1);
		}
		String jsonString = JSON.toJSONString(machineTypeList);
		System.out.println(jsonString);
		return  jsonString;
	}

	@GetMapping("/getMoudle/{id}")
	@ApiOperation(value = "根据typeid获取模块", notes = "传入machineTypeid")
	public Response<MachineTypeFormDataVo> getMoudleById(@PathVariable String id) {
		MachineTypeFormDataVo formDataVo = new MachineTypeFormDataVo();
		// 从表数据
		formDataVo.setMachineModuleVoList(BeanUtil.copyList(machineTypeService.getMachineModuleByParentId(id), MachineModuleVo.class));
		return Response.ok(formDataVo);
	}

}
