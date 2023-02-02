package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.base.dto.GetLogPageListDto;
import com.xjrsoft.module.base.service.IXjrBaseLogService;
import com.xjrsoft.module.base.vo.LogPageListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@RequestMapping("/log")
@AllArgsConstructor
@Api(value = "/log",tags = "日志模块")
@RestController
public class XjrBaseLogController {

    IXjrBaseLogService xjrBaseLogService;

    @GetMapping
    @ApiOperation(value="获取日志分页列表")
    @ApiImplicitParam(name = "入参",value = "dto",dataType = "GetLogPageListDto")
    public Response<PageOutput<LogPageListVo>> getLogPageList(GetLogPageListDto dto){
        return Response.ok(xjrBaseLogService.getLogPageList(dto)) ;
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除日志")
    @ApiImplicitParam(name = "日志Id",value = "ids",required = true,dataType = "string")
    public Response deleteLog(@PathVariable String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        return Response.ok(xjrBaseLogService.removeByIds(idList));
    }


    @DeleteMapping("/clear/{type}")
    @ApiOperation(value="删除日志")
    @ApiImplicitParam(name = "清除类型",value = "type",required = true,dataType = "int")
    public Response<Boolean> clearLog(@PathVariable int type){
        return Response.ok(xjrBaseLogService.clearLog(type));
    }
}
