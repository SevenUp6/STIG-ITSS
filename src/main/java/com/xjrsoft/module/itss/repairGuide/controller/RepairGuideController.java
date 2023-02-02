package com.xjrsoft.module.itss.repairGuide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.page.ConventPage;
import java.util.List;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.module.base.entity.XjrBaseAnnexesFile;
import com.xjrsoft.module.base.vo.AnnexesFileVo;
import com.xjrsoft.module.itss.repairGuide.dto.RepairGuideDto;
import com.xjrsoft.module.itss.repairGuide.dto.SaveRepairGuideFormDataDto;
import com.xjrsoft.module.itss.repairGuide.vo.RepairGuideFormDataVo;
import com.xjrsoft.module.itss.repairGuide.vo.RepairGuideListVo;
import com.xjrsoft.module.itss.repairGuide.dto.RepairGuideListDto;
import com.xjrsoft.module.itss.repairGuide.vo.XjrBaseAnnexesfileVo;
import com.xjrsoft.module.itss.repairGuide.entity.XjrBaseAnnexesfile;
import com.xjrsoft.module.itss.repairGuide.dto.XjrBaseAnnexesfileDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import com.xjrsoft.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.xjrsoft.module.itss.repairGuide.entity.RepairGuide;
import com.xjrsoft.module.itss.repairGuide.vo.RepairGuideVo;
import com.xjrsoft.module.itss.repairGuide.service.IRepairGuideService;

/**
 * 维修指南表 控制器
 *
 * @author hanhe
 * @since 2022-10-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/repairguide")
@Api(value = "维修指南表", tags = "维修指南表接口")
public class RepairGuideController {


	private final IRepairGuideService repairGuideService;


	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入repairGuide")
	public Response<RepairGuideFormDataVo> getFormData(@PathVariable String id) {
		RepairGuideFormDataVo formDataVo = new RepairGuideFormDataVo();
		// 主表数据
		RepairGuide repairGuide = repairGuideService.getById(id);
		formDataVo.setRepairGuideVo(BeanUtil.copy(repairGuide, RepairGuideVo.class));
		// 从表数据
//		formDataVo.setXjrBaseAnnexesfileVoList(BeanUtil.copyList(repairGuideService.getXjrBaseAnnexesfileByParentId(id), XjrBaseAnnexesfileVo.class));
		List<XjrBaseAnnexesfile>  annexesFileList=BeanUtil.copyList(repairGuideService.getXjrBaseAnnexesfileByParentId(id), XjrBaseAnnexesfile.class);
		List<XjrBaseAnnexesfileVo> annexesFileVoList = BeanUtil.copyList(repairGuideService.getXjrBaseAnnexesfileByParentId(id), XjrBaseAnnexesfileVo.class);
		if (CollectionUtil.isNotEmpty(annexesFileList)) {
			String getFileApiUrlPrefix = "/annexes-files/";
			for (XjrBaseAnnexesfileVo annexesFileVo : annexesFileVoList) {
				annexesFileVo.setUrl(getFileApiUrlPrefix + annexesFileVo.getFId());
			}
		}
		formDataVo.setXjrBaseAnnexesfileVoList(annexesFileVoList);
		return Response.ok(formDataVo);
	}

	/**
	 * 自定义分页 维修指南表
	 */
	@GetMapping
	@ApiOperation(value = "分页", notes = "传入repairGuide")
	public Response<PageOutput<RepairGuideListVo>> getPageList(RepairGuideListDto listDto) {
		IPage<RepairGuide> page = repairGuideService.getPageList(listDto);
		List<RepairGuideListVo> records = BeanUtil.copyList(page.getRecords(), RepairGuideListVo.class);
		DataTransUtil.transListShowData(records);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 或全部数据无分页 维修指南表
	 */
	@GetMapping("/getDataList/{mod_id}")
	@ApiOperation(value = "无分页", notes = "传入repairGuide")
	public Response<List<RepairGuideListVo>> getDataList(@PathVariable String mod_id) {
		RepairGuideListDto listDto=new RepairGuideListDto();
		listDto.setMod_id(mod_id);
		IPage<RepairGuide> page = repairGuideService.getPageList(listDto);
		List<RepairGuideListVo> records = BeanUtil.copyList(page.getRecords(), RepairGuideListVo.class);
		DataTransUtil.transListShowData(records);
		return Response.ok(  records);
	}

	/**
	 * 新增 维修指南表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入repairGuide")
	public Response save(@RequestBody SaveRepairGuideFormDataDto formDto) {
		RepairGuideDto repairGuideDto = formDto.getRepairGuideDto();
		RepairGuide repairGuide = BeanUtil.copy(repairGuideDto, RepairGuide.class);
//		List<XjrBaseAnnexesfileDto> xjrBaseAnnexesfileDtoList = formDto.getXjrBaseAnnexesfileDto();
//		List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList = BeanUtil.copyList(xjrBaseAnnexesfileDtoList, XjrBaseAnnexesfile.class);
		String id = repairGuideService.addRepairGuide(repairGuide);
		if("error".equals(id)){
			return Response.ok(15200,id,"保存失败");
		}
		return Response.ok(0,id,"保存成功");
	}

	/**
	 * 修改 维修指南表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入repairGuide")
	public Response update(@PathVariable String id, @RequestBody SaveRepairGuideFormDataDto formDto) {
		RepairGuideDto repairGuideDto = formDto.getRepairGuideDto();
		RepairGuide repairGuide = BeanUtil.copy(repairGuideDto, RepairGuide.class);

//		List<XjrBaseAnnexesfileDto> xjrBaseAnnexesfileDtoList = formDto.getAnnexesfileDto_modify();
//		List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList = BeanUtil.copyList(xjrBaseAnnexesfileDtoList, XjrBaseAnnexesfile.class);
		return Response.status(repairGuideService.updateRepairGuide(id, repairGuide, null));
	}


	
	/**
	 * 删除 维修指南表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		return Response.status(repairGuideService.removeByIds(Func.toStrList(ids)));
	}

	
}
