package com.xjrsoft.module.base.controller;

import java.util.List;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.base.dto.SaveXjrBaseSubsystemFormDataDto;
import com.xjrsoft.module.base.dto.XjrBaseSubsystemDto;
import com.xjrsoft.module.base.dto.XjrBaseSubsystemListDto;
import com.xjrsoft.module.base.entity.XjrBaseSubsystem;
import com.xjrsoft.module.base.service.IXjrBaseSubsystemService;
import com.xjrsoft.module.base.vo.XjrBaseSubsystemListVo;
import com.xjrsoft.module.base.vo.XjrBaseSubsystemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import com.xjrsoft.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

/**
 * 子系统表 控制器
 *
 * @author Job
 * @since 2021-06-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/subsystem")
@Api(value = "子系统表", tags = "子系统表接口")
public class XjrBaseSubsystemController {

	private final IXjrBaseSubsystemService subsystemService;

	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入xjrBaseSubsystem")
	public Response<XjrBaseSubsystemVo> getFormData(@PathVariable String id) {
		// 主表数据
		XjrBaseSubsystem subsystem = subsystemService.getById(id);
		return Response.ok(BeanUtil.copy(subsystem, XjrBaseSubsystemVo.class));
	}

	/**
	 * 查询子系统表列表数据
	 */
	@GetMapping("/all")
	@ApiOperation(value = "不分页", notes = "传入listVo")
	public Response<List<XjrBaseSubsystemListVo>> getList(XjrBaseSubsystemListDto listDto) {
		List<XjrBaseSubsystem> subsystemList = subsystemService.getList(listDto);
		return Response.ok(BeanUtil.copyList(subsystemList, XjrBaseSubsystemListVo.class));
	}

	@GetMapping
	@ApiOperation("查询当前用户授权的子系统")
	public Response<List<XjrBaseSubsystemVo>> getAuthList() {
		List<XjrBaseSubsystem> subsystemList = subsystemService.getCurAuthList();
		return Response.ok(BeanUtil.copyList(subsystemList, XjrBaseSubsystemVo.class));
	}

	/**
	 * 新增 子系统表
	 */
	@PostMapping
	@ApiOperation(value = "新增", notes = "传入xjrBaseSubsystem")
	public Response save(@RequestBody SaveXjrBaseSubsystemFormDataDto formDto) {
		XjrBaseSubsystemDto subsystemDto = formDto.getSubsystemDto();
		XjrBaseSubsystem subsystem = BeanUtil.copy(subsystemDto, XjrBaseSubsystem.class);
		return Response.status(subsystemService.addXjrBaseSubsystem(subsystem));
	}

	/**
	 * 修改 子系统表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入xjrBaseSubsystem")
	public Response update(@PathVariable String id, @RequestBody SaveXjrBaseSubsystemFormDataDto formDto) {
		XjrBaseSubsystemDto subsystemDto = formDto.getSubsystemDto();
		XjrBaseSubsystem subsystem = BeanUtil.copy(subsystemDto, XjrBaseSubsystem.class);
		return Response.status(subsystemService.updateXjrBaseSubsystem(id, subsystem));
	}

	/**
	 * 删除 子系统表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		return Response.status(subsystemService.removeByIds(Func.toStrList(ids)));
	}
}
