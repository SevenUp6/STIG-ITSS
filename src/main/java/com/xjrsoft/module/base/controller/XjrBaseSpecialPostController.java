package com.xjrsoft.module.base.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.base.dto.SaveSpecialPostDto;
import com.xjrsoft.module.base.dto.SpecialPostDto;
import com.xjrsoft.module.base.entity.XjrBaseSpecialPost;
import com.xjrsoft.module.base.service.IXjrBaseSpecialPostService;
import com.xjrsoft.module.base.vo.SpecialPostVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/special-post")
@Api(value = "/special-post",tags = "特殊角色模块")
public class XjrBaseSpecialPostController {

    private IXjrBaseSpecialPostService specialPostService;

    @GetMapping
    @ApiOperation(value="获取公司列表分页")
    public Response<List<SpecialPostVo>> getSpecialPostList(@ApiParam("类型：1-公司，2-部门") Integer type) {
        List<XjrBaseSpecialPost> specialPostList = specialPostService.getSpecialPostList(type);
        List<SpecialPostVo> resultList = BeanUtil.copyList(specialPostList.stream().collect(Collectors.toList()), SpecialPostVo.class);
        return Response.ok(resultList, Response.MSG_QUERY_SUCCESSFUL);
    }

    @PostMapping
    @ApiOperation(value="获取公司列表分页")
    public Response submit(@RequestBody SaveSpecialPostDto saveSpecialPostDto) {
        Integer type = saveSpecialPostDto.getType();
        List<SpecialPostDto> specialPostDtoList = saveSpecialPostDto.getSpecialPostDtoList();
        List<XjrBaseSpecialPost> specialPostList = BeanUtil.copyList(specialPostDtoList, XjrBaseSpecialPost.class);
        return Response.status(specialPostService.submit(specialPostList, type));
    }
}
