package com.xjrsoft.module.itss.machineCompany.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.module.itss.machineCompany.dto.MachineCompanyDto;
import com.xjrsoft.module.itss.machineCompany.dto.MachineCompanyListDto;
import com.xjrsoft.module.itss.machineCompany.dto.SaveMachineCompanyFormDataDto;
import com.xjrsoft.module.itss.machineCompany.entity.MachineCompany;
import com.xjrsoft.module.itss.machineCompany.service.IMachineCompanyService;
import com.xjrsoft.module.itss.machineCompany.vo.MachineCompanyListVo;
import com.xjrsoft.module.itss.machineCompany.vo.MachineCompanyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备所属单位表 控制器
 *
 * @author hanhe
 * @since 2022-10-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinecompany")
@Api(value = "设备所属单位表", tags = "设备所属单位表接口")
public class MachineCompanyController {


	private final IMachineCompanyService machineCompanyService;


	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入machineCompany")
	public Response<MachineCompanyVo> getFormData(@PathVariable String id) {
		// 主表数据
		MachineCompany machineCompany = machineCompanyService.getById(id);
		return Response.ok(BeanUtil.copy(machineCompany, MachineCompanyVo.class));
	}

	/**
	 * 自定义分页 设备所属单位表
	 */
	@GetMapping
	@ApiOperation(value = "分页", notes = "传入machineCompany")
	public Response<PageOutput<MachineCompanyListVo>> getPageList(MachineCompanyListDto listDto) {
		IPage<MachineCompany> page = machineCompanyService.getPageList(listDto);
		List<MachineCompanyListVo> records = BeanUtil.copyList(page.getRecords(), MachineCompanyListVo.class);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 新增 设备所属单位表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入machineCompany")
	public Response save(@RequestBody SaveMachineCompanyFormDataDto formDto) {
		MachineCompanyDto machineCompanyDto = formDto.getMachineCompanyDto();
		MachineCompany machineCompany = BeanUtil.copy(machineCompanyDto, MachineCompany.class);
		List companylist=machineCompanyService.getListByName(machineCompany.getComName());
		if(null !=companylist && companylist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的公司名称【"+machineCompany.getComName()+"】");
		}
		String comcode=machineCompany.getComCode();
		String  maxComCode;
		MachineCompany maxCompany = machineCompanyService.getMaxComCodeByPid(machineCompany.getId());//(machineCompany.getId()是pid
		if(null==maxCompany){
			maxComCode=machineCompany.getPcode()+"001";
			machineCompany.setComCode(maxComCode);
		}else{
			maxComCode=maxCompany.getComCode();
			String code=maxComCode.substring(0,maxComCode.length()-3);
			String maxcode=maxComCode.substring(maxComCode.length()-3,maxComCode.length());
			String newcode =Integer.parseInt(maxcode)+1+"";
			if(newcode.length()==1){
				newcode="00"+newcode;
			}else if(newcode.length()==2){
				newcode="0"+newcode;
			}
			machineCompany.setComCode(code+newcode);
		}
		String dataId = machineCompanyService.addMachineCompany(machineCompany);
		return dataId == null ? Response.notOk() : Response.ok(dataId);
	}

	/**
	 * 修改 设备所属单位表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入machineCompany")
	public Response update(@PathVariable String id, @RequestBody SaveMachineCompanyFormDataDto formDto) {
		MachineCompanyDto machineCompanyDto = formDto.getMachineCompanyDto();
		MachineCompany machineCompany = BeanUtil.copy(machineCompanyDto, MachineCompany.class);
		List companylist=machineCompanyService.getListByName(machineCompany.getComName());
		if(null !=companylist && companylist.size()>0){
			return Response.notOk(1002,"保存失败，已存在相同的公司名称【"+machineCompany.getComName()+"】");
		}
		return Response.status(machineCompanyService.updateMachineCompany(id, machineCompany));
	}


	
	/**
	 * 删除 设备所属单位表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		return Response.status(machineCompanyService.removeByIds(Func.toStrList(ids)));
	}
	/**
	 * 获取全量公司数据
	 */
	@GetMapping("/getalldata")
	@ApiOperation(value = "分页", notes = "传入machineCompany")
	public String getAllData() {
		MachineCompanyListDto listDto1=new MachineCompanyListDto();
		MachineCompanyListDto listDto2=new MachineCompanyListDto();
		MachineCompanyListDto listDto3=new MachineCompanyListDto();

		//先获取一级公司
		listDto1.setPcode("000");
		IPage<MachineCompany> page = machineCompanyService.getPageList(listDto1);
		List<MachineCompanyDto> company = BeanUtil.copyList(page.getRecords(), MachineCompanyDto.class);

		List<MachineCompanyDto> companyList = new ArrayList<>();
		for (MachineCompanyDto machineCompany : company) {
			MachineCompanyDto machineCompany1 = new MachineCompanyDto();
			machineCompany1.setId(machineCompany.getId());
			machineCompany1.setComCode(machineCompany.getComCode());
			machineCompany1.setComName(machineCompany.getComName());
			machineCompany1.setPcode(machineCompany.getPcode());
			machineCompany1.setPname(machineCompany.getPname());
			machineCompany1.setNodeName(machineCompany.getNodeName());
			////获取二级公司
			listDto2.setPcode(machineCompany1.getComCode());
			IPage<MachineCompany> page2 = machineCompanyService.getPageList(listDto2);
			List<MachineCompanyDto> company2 = BeanUtil.copyList(page2.getRecords(), MachineCompanyDto.class);

			List<MachineCompanyDto> companyList2 = new ArrayList<>();
			for (MachineCompanyDto machineCompany_f : company2) {
				MachineCompanyDto machineCompany2 = new MachineCompanyDto();
				machineCompany2.setId(machineCompany_f.getId());
				machineCompany2.setComCode(machineCompany_f.getComCode());
				machineCompany2.setComName(machineCompany_f.getComName());
				machineCompany2.setPcode(machineCompany_f.getPcode());
				machineCompany2.setPname(machineCompany_f.getPname());
				machineCompany2.setNodeName(machineCompany_f.getNodeName());
				////获取三级公司
				listDto3.setPcode(machineCompany2.getComCode());
				IPage<MachineCompany> page3 = machineCompanyService.getPageList(listDto3);
				List<MachineCompanyDto> company3 = BeanUtil.copyList(page3.getRecords(), MachineCompanyDto.class);

				machineCompany2.setCompanies(company3);
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
