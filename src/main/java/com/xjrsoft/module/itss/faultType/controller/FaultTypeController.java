package com.xjrsoft.module.itss.faultType.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.page.ConventPage;

import java.util.ArrayList;
import java.util.List;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeDto;
import com.xjrsoft.module.itss.faultType.dto.SaveFaultTypeFormDataDto;
import com.xjrsoft.module.itss.faultType.vo.FaultTypeVo;
import com.xjrsoft.module.itss.faultType.vo.FaultTypeListVo;
import com.xjrsoft.module.itss.faultType.dto.FaultTypeListDto;
import com.xjrsoft.module.itss.machineCompany.dto.MachineCompanyDto;
import com.xjrsoft.module.itss.machineCompany.dto.MachineCompanyListDto;
import com.xjrsoft.module.itss.machineCompany.dto.SaveMachineCompanyFormDataDto;
import com.xjrsoft.module.itss.machineCompany.entity.MachineCompany;
import com.xjrsoft.module.itss.machineModule.dto.MachineModuleListDto;
import com.xjrsoft.module.itss.machineModule.entity.MachineModule;
import com.xjrsoft.module.itss.machineModule.service.IMachineModuleService;
import com.xjrsoft.module.itss.machineType.dto.MachineTypeListDto;
import com.xjrsoft.module.itss.machineType.entity.MachineType;
import com.xjrsoft.module.itss.machineType.service.IMachineTypeService;
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
	private final IMachineTypeService machineTypeService;

	private final IMachineModuleService machineModuleService;


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
	public Response save(@RequestBody SaveMachineCompanyFormDataDto formDto) {
		MachineCompanyDto machineCompanyDto = formDto.getMachineCompanyDto();
		MachineCompany machineCompany = BeanUtil.copy(machineCompanyDto, MachineCompany.class);
		FaultType faultType=new FaultType();
		MachineModule machineModule=new MachineModule();
		boolean isSuccess;
		//先判断是不是设备种类
		MachineType machineType = machineTypeService.getById(machineCompany.getPcode());
		if(null!=machineType){//是设备种类，则加的是模块
			List typelist=machineModuleService.getListByNamePid(machineCompany.getPcode(),machineCompany.getComName());
			if(null !=typelist && typelist.size()>0){
				return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+machineModule.getModName()+"】");
			}
			machineModule.setTypeId(machineCompany.getPcode());
			machineModule.setTypeName(machineCompany.getPname());
			machineModule.setModName(machineCompany.getComName());
			isSuccess = machineModuleService.addMachineModule(machineModule, null);
		}else{ //不是类型就说明是模块，加的是故障类型
			List typelist=faultTypeService.getListByNamePid(machineCompany.getPcode(),machineCompany.getComName());
			if(null !=typelist && typelist.size()>0){
				return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+faultType.getFauName()+"】");
			}
			faultType.setModId(machineCompany.getId());
			faultType.setModName(machineCompany.getPname());
			faultType.setFauName(machineCompany.getComName());
			MachineModule moudle=machineModuleService.getById(machineCompany.getId());
			faultType.setTypeId(moudle.getTypeId());
			faultType.setTypeName(moudle.getTypeName());
			isSuccess = faultTypeService.addFaultType(faultType);
		}

//		FaultTypeDto faultTypeDto = formDto.getFaultTypeDto();
//		FaultType faultType = BeanUtil.copy(faultTypeDto, FaultType.class);
		return Response.status(isSuccess);
	}

	/**
	 * 修改 故障类型表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入faultType")
	public Response update(@PathVariable String id, @RequestBody SaveMachineCompanyFormDataDto formDto) {
		MachineCompanyDto machineCompanyDto = formDto.getMachineCompanyDto();
		MachineCompany machineCompany = BeanUtil.copy(machineCompanyDto, MachineCompany.class);
		FaultType faultType=new FaultType();
		MachineModule machineModule=new MachineModule();
		boolean isSuccess;
		//先判断是不是设备种类
		MachineType machineType = machineTypeService.getById(machineCompany.getPcode());
		if(null!=machineType){//是设备种类，则加的是模块
			List typelist=machineModuleService.getListByNamePid(machineCompany.getPcode(),machineCompany.getComName());
			if(null !=typelist && typelist.size()>0){
				return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+machineModule.getModName()+"】");
			}
			machineModule.setModName(machineCompany.getComName());
			isSuccess = machineModuleService.updateMachineModule(id,machineModule,null);
		}else{ //不是类型就说明是模块，加的是故障类型
			List typelist=faultTypeService.getListByNamePid(machineCompany.getPcode(),machineCompany.getComName());
			if(null !=typelist && typelist.size()>0){
				return Response.notOk(1002,"保存失败，已存在相同的设备模块名称【"+faultType.getFauName()+"】");
			}
			faultType.setFauName(machineCompany.getComName());
			isSuccess = faultTypeService.updateFaultType(id, faultType);
		}

		return Response.status(isSuccess);
	}


	
	/**
	 * 删除 故障类型表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		boolean flag;
		flag=machineModuleService.removeByIds(Func.toStrList(ids));
		if(!flag){
			flag=machineTypeService.removeByIds(Func.toStrList(ids));
			if(!flag) {
				flag=faultTypeService.removeByIds(Func.toStrList(ids));
			}
		}
		return Response.status(flag);
	}

	/**
	 * 获取故障数据
	 */
	@GetMapping("/getalldata")
	@ApiOperation(value = "分页", notes = "传入machineCompany")
	public String getAllData() {
		MachineTypeListDto listDto1=new MachineTypeListDto();
		MachineModuleListDto listDto2=new MachineModuleListDto();
		FaultTypeListDto listDto3=new FaultTypeListDto();

		//先获取一级公司
		List<MachineType> machineTypeList = machineTypeService.getList();
//		List<MachineCompanyDto> company = BeanUtil.copyList(page, MachineCompanyDto.class);

		List<MachineCompanyDto> companyList = new ArrayList<>();
		for (MachineType machineType : machineTypeList) {
			MachineCompanyDto machineCompany1 = new MachineCompanyDto();
			machineCompany1.setId(machineType.getId());
			machineCompany1.setComCode(machineType.getId());
			machineCompany1.setComName(machineType.getMachName());
			machineCompany1.setPcode("000");
			machineCompany1.setPname("Pname");
//			machineCompany1.setNodeName(machineCompany.getNodeName());

			//获取二级公司
			listDto2.setType_id(machineCompany1.getId());
			IPage<MachineModule> page2 = machineModuleService.getPageList(listDto2);
			List<MachineModule> company2 = BeanUtil.copyList(page2.getRecords(), MachineModule.class);

			List<MachineCompanyDto> companyList2 = new ArrayList<>();
			for (MachineModule machineModule : company2) {
				MachineCompanyDto machineCompany2 = new MachineCompanyDto();
				machineCompany2.setId(machineModule.getId());
				machineCompany2.setComCode(machineModule.getId());
				machineCompany2.setComName(machineModule.getModName());
				machineCompany2.setPcode(machineCompany1.getComCode());
				machineCompany2.setPname(machineCompany1.getComName());
//				machineCompany2.setNodeName(machineModule.getNodeName());

				////获取三级公司
				listDto3.setMod_id(machineCompany2.getId());
				IPage<FaultType> page3 = faultTypeService.getPageList(listDto3);
				List<FaultType> company3 = BeanUtil.copyList(page3.getRecords(), FaultType.class);
				List<MachineCompanyDto> companyList3 = new ArrayList<>();
				for (FaultType faultType : company3) {
					MachineCompanyDto machineCompany3 = new MachineCompanyDto();
					machineCompany3.setId(faultType.getId());
					machineCompany3.setComCode(faultType.getId());
					machineCompany3.setComName(faultType.getFauName());
					machineCompany3.setPcode(machineCompany2.getComCode());
					machineCompany3.setPname(machineCompany2.getComName());
					companyList3.add(machineCompany3);
				}
				machineCompany2.setCompanies(companyList3);
				companyList2.add(machineCompany2);
			}
			machineCompany1.setCompanies(companyList2);
			companyList.add(machineCompany1);
		}
		String jsonString = JSON.toJSONString(companyList);
		System.out.println(jsonString);
		return  jsonString;
	}



}
