package com.xjrsoft.module.demo.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.demo.entity.XjrBaseItem;
import com.xjrsoft.module.demo.service.IXjrBaseItemService;
import com.xjrsoft.module.demo.vo.ItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/item")
@Api(value = "/item", tags = "商品模块")
public class XjrBaseItemController {

    private final IXjrBaseItemService itemService;

    @GetMapping
    @ApiOperation(value = "获取商品列表")
    public Response<List<ItemVo>> getList(){
        return Response.ok(BeanUtil.copyList(itemService.list(), ItemVo.class));
    }
}
