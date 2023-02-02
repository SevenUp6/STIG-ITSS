package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.dto.CodeSchemaDto;
import com.xjrsoft.module.base.dto.CodeSchemaListDto;
import com.xjrsoft.module.base.entity.XjrBaseCodeschema;
import com.xjrsoft.module.base.service.IXjrBaseCodeSchemaService;
import com.xjrsoft.module.base.vo.CodeSchemaListVo;
import com.xjrsoft.module.base.vo.CodeSchemaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 代码模板 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@AllArgsConstructor
@RestController
@RequestMapping("/code-schema")
@Api(value = "/code-rule",tags = "代码模板")
public class XjrBaseCodeschemaController {
    private IXjrBaseCodeSchemaService codeSchemeService;

    @ApiOperation(value="获取代码模板分页")
    @GetMapping
    public Response<PageOutput<CodeSchemaListVo>> getCodeSchemaPageList(CodeSchemaListDto dto){
        return Response.ok(codeSchemeService.getCodeSchemaPageList(dto));
    }

    @GetMapping("/{id}")
    @ApiOperation(value="根据id 查询代码模板详情")
    @ApiImplicitParam(name = "代码模板id",value = "id",required = true,dataType = "string")
    public Response<CodeSchemaVo> getCodeSchemaById(@PathVariable String id){
        XjrBaseCodeschema codeSchema = codeSchemeService.getById(id);
        return Response.ok(BeanUtil.copy(codeSchema, CodeSchemaVo.class));
    }

    @PostMapping
    @ApiOperation(value="新增代码模板")
    public Response addCodeSchema(@RequestBody CodeSchemaDto codeSchemaDto) {
        XjrBaseCodeschema codeSchema = BeanUtil.copy(codeSchemaDto, XjrBaseCodeschema.class);
        return Response.status(codeSchemeService.save(codeSchema));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改代码模板")
    @ApiImplicitParam(name = "编码规则id", value = "id", required = true, dataType = "string")
    public Response updateCodeSchema(@PathVariable String id, @RequestBody CodeSchemaDto codeSchemaDto) {
        XjrBaseCodeschema codeSchema = BeanUtil.copy(codeSchemaDto, XjrBaseCodeschema.class);
        codeSchema.setId(id);
        return Response.status(codeSchemeService.updateById(codeSchema));
    }
    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除代码模板")
    @ApiImplicitParam(name = "行政区域id,多个用逗号隔开",value = "id",required = true,dataType = "string")
    public Response deleteCodeRule(@PathVariable String ids){
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        if (idsArray.length > 1) {
            return Response.status(codeSchemeService.removeByIds(Arrays.asList(idsArray)));
        } else {
            return Response.status(codeSchemeService.removeById(ids));
        }
    }
}
