package com.xjrsoft.module.demo.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.demo.service.IXjrBaseBuyOrderService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/buy-order")
@Api(value = "/buy-order", tags = "订单模块")
public class XjrBaseBuyOrderController {

    private final IXjrBaseBuyOrderService buyOrderService;
}
