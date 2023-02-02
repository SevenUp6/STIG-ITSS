package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.dto.CodeSchemaDto;
import com.xjrsoft.module.base.dto.DataRelationDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDatarelation;
import com.xjrsoft.module.base.service.IXjrBaseDatarelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 数据权限对应表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@AllArgsConstructor
@RestController
@RequestMapping("/data-relation")
@Api(value = "/code-rule",tags = "数据权限")
public class XjrBaseDatarelationController {
    private IXjrBaseDatarelationService datarelationService;

    @ApiOperation(value="获取数据权限分页")
    @GetMapping("/page")
    public Response<PageOutput<XjrBaseDatarelation>> getDataRelationPageList(GetPageListDto dto){
        return Response.ok(datarelationService.getDataRelationPageList(dto));
    }

    @PostMapping
    @ApiOperation(value="新增数据权限")
    public Response addCodeSchema(@RequestBody CodeSchemaDto codeSchemaDto) {
        XjrBaseDatarelation dataRelation = BeanUtil.copy(codeSchemaDto, XjrBaseDatarelation.class);
        return Response.status(datarelationService.addDataRelation(dataRelation));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改数据权限")
    @ApiImplicitParam(name = "编码规则id", value = "id", required = true, dataType = "string")
    public Response updateDataRelation(@PathVariable String id, @RequestBody DataRelationDto dataRelationDto) {
        XjrBaseDatarelation dataRelation = BeanUtil.copy(dataRelationDto, XjrBaseDatarelation.class);
        Integer count = datarelationService.count(Wrappers.<XjrBaseDatarelation>query().lambda().eq(XjrBaseDatarelation::getId, id));
        if (count > 0) {
            return Response.status(datarelationService.updateDataRelation(id, dataRelation));
        }
        return Response.notOk("该行政区域不存在！");
    }
    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除数据权限")
    @ApiImplicitParam(name = "行政区域id,多个用逗号隔开",value = "id",required = true,dataType = "string")
    public Response deleteDataRelation(@PathVariable String ids){
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        if (idsArray.length > 1) {
            return Response.status(datarelationService.removeByIds(Arrays.asList(idsArray)));
        } else {
            return Response.status(datarelationService.removeById(ids));
        }
    }
}
