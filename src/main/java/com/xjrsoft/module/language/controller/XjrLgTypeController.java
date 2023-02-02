package com.xjrsoft.module.language.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.language.dto.GetListLgTypeDto;
import com.xjrsoft.module.language.dto.LgTypeDto;
import com.xjrsoft.module.language.entity.XjrLgType;
import com.xjrsoft.module.language.service.IXjrLgTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 多语言语言类型表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/lg-type")
@Api(value = "/lg-type", tags = "语言类型模块")
public class XjrLgTypeController {

    private IXjrLgTypeService lgTypeService;

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:获取详情
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取详情")
    public Response getLgTypeById(@PathVariable String id) {
        return Response.ok(lgTypeService.getById(id));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[id, xjrLgType]
    * @return:com.xjrsoft.common.result.Response
    * @Description:修改
    */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    public Response updateLgType(@PathVariable String id, @RequestBody LgTypeDto lgTypeDto) {
        lgTypeDto.setId(id);
        return Response.status(lgTypeService.updateById(BeanUtil.copy(lgTypeDto, XjrLgType.class)));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[id, xjrLgType]
    * @return:com.xjrsoft.common.result.Response
    * @Description:设置为主语言
    */
    @PatchMapping("/{id}")
    @ApiOperation(value = "设置为主语言")
    public Response setMainLanguage(@PathVariable String id) {
        return Response.status(lgTypeService.setMainlanguage(id));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:获取列表数据
    */
    @GetMapping
    @ApiOperation(value = "获取列表数据")
    public Response getPageData(GetListLgTypeDto dto) {
        return Response.ok(lgTypeService.getPageData(dto));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[id, xjrLgType]
    * @return:com.xjrsoft.common.result.Response
    * @Description:新增
    */
    @PostMapping
    @ApiOperation(value = "新增")
    public Response saveLgType(@RequestBody XjrLgType xjrLgType) {
        xjrLgType.setIsMain(0);
        return Response.status(lgTypeService.save(xjrLgType));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[xjrLgType]
    * @return:com.xjrsoft.common.result.Response
    * @Description:删除
    */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除")
    public Response deleteType(@PathVariable String ids) {
        return Response.status(lgTypeService.removeByIds(Arrays.asList(ids.split(","))));
    }

}
