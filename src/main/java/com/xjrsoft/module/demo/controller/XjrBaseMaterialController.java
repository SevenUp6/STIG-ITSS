package com.xjrsoft.module.demo.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.demo.service.IXjrBaseMaterialService;
import com.xjrsoft.module.demo.vo.ItemVo;
import com.xjrsoft.module.demo.vo.MaterialVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 物料表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/material")
@Api(value = "/material", tags = "物料模块")
public class XjrBaseMaterialController {

    private final IXjrBaseMaterialService materialService;

    @GetMapping
    @ApiOperation(value = "获取物料列表")
    public Response<List<MaterialVo>> getList(){
        return Response.ok(BeanUtil.copyList(materialService.list(), MaterialVo.class));
    }
}
