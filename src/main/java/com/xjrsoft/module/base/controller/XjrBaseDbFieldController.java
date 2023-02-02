package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.dto.DbFieldDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDbField;
import com.xjrsoft.module.base.service.IXjrBaseDbFieldService;
import com.xjrsoft.module.base.vo.DataSourceVo;
import com.xjrsoft.module.base.vo.DbFieldVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 建表常用字段 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/database-field")
@AllArgsConstructor
@Api(value = "/database-field", tags = "数据库常用字段模块")
public class XjrBaseDbFieldController {

    private IXjrBaseDbFieldService dbFieldService;

    @GetMapping("/{id}")
    @ApiOperation(value="获取数据库常用字段详情")
    public Response getDbFieldById(@PathVariable String id){
        XjrBaseDbField dbField = dbFieldService.getById(id);
        return Response.ok(BeanUtil.copy(dbField, DbFieldVo.class));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改数据库常用字段")
    public Response updateDbField(@PathVariable String id, @RequestBody DbFieldDto dbFieldDto) {
        XjrBaseDbField dbField = BeanUtil.copy(dbFieldDto, XjrBaseDbField.class);
        dbField.setId(id);
        return Response.status(dbFieldService.updateById(dbField));
    }

    @GetMapping
    @ApiOperation(value = "获取数据库常用字段列表，分页")
    public Response getDbFieldPageList(GetPageListDto pageListDto) {
        return Response.ok(dbFieldService.getDbDraftPageList(pageListDto));
    }

    @PostMapping
    @ApiOperation(value = "新增数据库常用字段")
    public Response addDbField(@RequestBody DbFieldDto dbFieldDto) {
        XjrBaseDbField dbField = BeanUtil.copy(dbFieldDto, XjrBaseDbField.class);
        return Response.ok(dbFieldService.save(dbField));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除数据库常用字段")
    public Response deleteDbField(@PathVariable String ids) {
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idArray.length == 1) {
            isSuccess = dbFieldService.removeById(ids);
        } else {
            isSuccess = dbFieldService.removeByIds(Arrays.asList(idArray));
        }
        return Response.status(isSuccess);
    }
}
