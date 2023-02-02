package com.xjrsoft.module.base.controller;

import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.constants.DataAuthConstants;
import com.xjrsoft.module.base.dto.AuthorizeDataDto;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;
import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.xjrsoft.module.base.service.IXjrBaseDataAuthorizeService;
import com.xjrsoft.module.base.service.IXjrBaseModuleService;
import com.xjrsoft.module.base.vo.DataAuthMenuVo;
import com.xjrsoft.module.base.vo.DataAuthorizedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 数据授权表 控制器
 *
 * @author job
 * @since 2021-01-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/data-authorize")
@Api(value = "数据授权模块", tags = "数据授权接口")
public class XjrBaseDataAuthorizeController {

	private final IXjrBaseDataAuthorizeService dataAuthorizeService;

	private final IXjrBaseModuleService moduleService;

	@GetMapping("/{objectId}/{objectType}/menu")
	@ApiOperation(value="根据用户id获取其所有授权的菜单id")
	public Response<List<DataAuthMenuVo>> getAuthorizedDataListForUser(@PathVariable String objectId, @PathVariable Integer objectType,
																	   @RequestParam(name = "systemCode", required = false, defaultValue = "0") String systemId) {
		List<XjrBaseModule> moduleList = moduleService.getAuthModuleForObject(objectId, objectType, systemId);
		List<DataAuthMenuVo> authMenuVoList = BeanUtil.copyList(moduleList, DataAuthMenuVo.class);
		List<XjrBaseDataAuthorize> dataAuthorizeList = dataAuthorizeService.getAuthorizedDataListForObject(objectId, objectType);
		List<DataAuthorizedVo> dataAuthorizedVoList = BeanUtil.copyList(dataAuthorizeList, DataAuthorizedVo.class);
		List<DataAuthMenuVo> filteredModuleList = new ArrayList<>();
		for (DataAuthMenuVo authMenuVo: authMenuVoList) {
			if (CollectionUtil.contains(DataAuthConstants.NO_ENABLED_MODULES, authMenuVo.getEnCode())) {
				continue;
			}
			filteredModuleList.add(authMenuVo);
			String moduleId = authMenuVo.getModuleId();
			for (DataAuthorizedVo dataAuthorizedVo: dataAuthorizedVoList) {
				if (StringUtil.equals(moduleId, dataAuthorizedVo.getModuleId())) {
					authMenuVo.setDataAuthorizedVo(dataAuthorizedVo);
					break;
				}
			}
		}
		ForestNodeMerger.merge(filteredModuleList);
		List<DataAuthMenuVo> resultList = new ArrayList<>();
		for (DataAuthMenuVo dataAuthMenuVo : filteredModuleList) {
			if (StringUtil.equalsIgnoreCase(dataAuthMenuVo.getTarget(), "iframe") || CollectionUtil.isNotEmpty(dataAuthMenuVo.getChildren())) {
				resultList.add(dataAuthMenuVo);
				dataAuthMenuVo.setChildren(null);
			}
		}
		return Response.ok(ForestNodeMerger.merge(resultList));
	}

	@PutMapping
	@ApiOperation(value = "修改数据授权")
	public Response authorizeData(@RequestBody AuthorizeDataDto authorizeDataDto) {
		XjrBaseDataAuthorize dataAuthorize = BeanUtil.copy(authorizeDataDto, XjrBaseDataAuthorize.class);
		Set<String> moduleIdList = authorizeDataDto.getModuleIdList();
		return Response.status(dataAuthorizeService.authorizeData(dataAuthorize, moduleIdList));
	}
}
