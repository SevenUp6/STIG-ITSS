package com.xjrsoft.module.itss.fAQuestion.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.page.ConventPage;
import java.util.List;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.module.itss.fAQuestion.dto.FAQuestionDto;
import com.xjrsoft.module.itss.fAQuestion.dto.SaveFAQuestionFormDataDto;
import com.xjrsoft.module.itss.fAQuestion.vo.FAQuestionFormDataVo;
import com.xjrsoft.module.itss.fAQuestion.vo.FAQuestionListVo;
import com.xjrsoft.module.itss.fAQuestion.dto.FAQuestionListDto;
import com.xjrsoft.module.itss.fAQuestion.vo.XjrBaseAnnexesfileVo;
import com.xjrsoft.module.itss.fAQuestion.entity.XjrBaseAnnexesfile;
import com.xjrsoft.module.itss.fAQuestion.dto.XjrBaseAnnexesfileDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import com.xjrsoft.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.xjrsoft.module.itss.fAQuestion.entity.FAQuestion;
import com.xjrsoft.module.itss.fAQuestion.vo.FAQuestionVo;
import com.xjrsoft.module.itss.fAQuestion.service.IFAQuestionService;

/**
 * 常见问题表 控制器
 *
 * @author hanhe
 * @since 2022-10-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/faquestion")
@Api(value = "常见问题表", tags = "常见问题表接口")
public class FAQuestionController {


	private final IFAQuestionService fAQuestionService;


	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入fAQuestion")
	public Response<FAQuestionFormDataVo> getFormData(@PathVariable String id) {
		FAQuestionFormDataVo formDataVo = new FAQuestionFormDataVo();
		// 主表数据
		FAQuestion fAQuestion = fAQuestionService.getById(id);
		formDataVo.setFAQuestionVo(BeanUtil.copy(fAQuestion, FAQuestionVo.class));
		// 从表数据
//		formDataVo.setXjrBaseAnnexesfileVoList(BeanUtil.copyList(fAQuestionService.getXjrBaseAnnexesfileByParentId(id), XjrBaseAnnexesfileVo.class));
		List<com.xjrsoft.module.itss.fAQuestion.entity.XjrBaseAnnexesfile>  annexesFileList=BeanUtil.copyList(fAQuestionService.getXjrBaseAnnexesfileByParentId(id), com.xjrsoft.module.itss.fAQuestion.entity.XjrBaseAnnexesfile.class);
		List<com.xjrsoft.module.itss.fAQuestion.vo.XjrBaseAnnexesfileVo> annexesFileVoList = BeanUtil.copyList(fAQuestionService.getXjrBaseAnnexesfileByParentId(id), com.xjrsoft.module.itss.fAQuestion.vo.XjrBaseAnnexesfileVo.class);
		if (CollectionUtil.isNotEmpty(annexesFileList)) {
			String getFileApiUrlPrefix = "/annexes-files/";
			for (com.xjrsoft.module.itss.fAQuestion.vo.XjrBaseAnnexesfileVo annexesFileVo : annexesFileVoList) {
				annexesFileVo.setUrl(getFileApiUrlPrefix + annexesFileVo.getFId());
			}
		}
		formDataVo.setXjrBaseAnnexesfileVoList(annexesFileVoList);
		return Response.ok(formDataVo);
	}

	/**
	 * 自定义分页 常见问题表
	 */
	@GetMapping
	@ApiOperation(value = "分页", notes = "传入fAQuestion")
	public Response<PageOutput<FAQuestionListVo>> getPageList(FAQuestionListDto listDto) {
		IPage<FAQuestion> page = fAQuestionService.getPageList(listDto);
		List<FAQuestionListVo> records = BeanUtil.copyList(page.getRecords(), FAQuestionListVo.class);
		DataTransUtil.transListShowData(records);
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}
	/**
	 * 无分页 常见问题表
	 */
	@GetMapping("/getDataList/{mod_id}")
	@ApiOperation(value = "无分页", notes = "传入fAQuestion")
	public Response<List<FAQuestionListVo>> getDataList(@PathVariable String mod_id) {
		FAQuestionListDto listDto=new FAQuestionListDto();
		listDto.setMod_id(mod_id);
		IPage<FAQuestion> page = fAQuestionService.getPageList(listDto);
		List<FAQuestionListVo> records = BeanUtil.copyList(page.getRecords(), FAQuestionListVo.class);
		return Response.ok( records);
	}

	/**
	 * 新增 常见问题表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入fAQuestion")
	public Response save(@RequestBody SaveFAQuestionFormDataDto formDto) {
		FAQuestionDto fAQuestionDto = formDto.getFAQuestionDto();
		FAQuestion fAQuestion = BeanUtil.copy(fAQuestionDto, FAQuestion.class);
//		List<XjrBaseAnnexesfileDto> xjrBaseAnnexesfileDtoList = formDto.getXjrBaseAnnexesfileDto();
//		List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList = BeanUtil.copyList(xjrBaseAnnexesfileDtoList, XjrBaseAnnexesfile.class);
		String id = fAQuestionService.addFAQuestion(fAQuestion);
		if("error".equals(id)){
			return Response.ok(15200,id,"保存失败");
		}
		return Response.ok(0,id,"保存成功");
	}

	/**
	 * 修改 常见问题表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入fAQuestion")
	public Response update(@PathVariable String id, @RequestBody SaveFAQuestionFormDataDto formDto) {
		FAQuestionDto fAQuestionDto = formDto.getFAQuestionDto();
		FAQuestion fAQuestion = BeanUtil.copy(fAQuestionDto, FAQuestion.class);

//		List<XjrBaseAnnexesfileDto> xjrBaseAnnexesfileDtoList = formDto.getXjrBaseAnnexesfileDto();
//		List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList = BeanUtil.copyList(xjrBaseAnnexesfileDtoList, XjrBaseAnnexesfile.class);
		return Response.status(fAQuestionService.updateFAQuestion(id, fAQuestion, null));
	}


	
	/**
	 * 删除 常见问题表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		return Response.status(fAQuestionService.removeByIds(Func.toStrList(ids)));
	}

	
}
