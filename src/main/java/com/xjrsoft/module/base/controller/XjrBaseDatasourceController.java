 package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.dto.DataSourceDto;
import com.xjrsoft.module.base.entity.XjrBaseDatasource;
import com.xjrsoft.module.base.service.IXjrBaseDatasourceService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.DataSourceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据源表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/data-sources")
@Api(value = "/data-sources",tags = "数据源模块")
@AllArgsConstructor
public class XjrBaseDatasourceController {

    private final IXjrBaseDatasourceService datasourceService;

    @GetMapping("/{id}")
    @ApiOperation(value="获取数据源详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string")
    public Response<DataSourceVo> getById(@PathVariable String id){
        return Response.ok(BeanUtil.copy(datasourceService.getById(id), DataSourceVo.class));
    }

    @GetMapping
    @ApiOperation(value="获取数据源列表-不分页")
    @ApiImplicitParam(name = "keyword",value = "keyword", dataType = "string")
    public Response<List<DataSourceVo>> getList(String keyword){
        return Response.ok(datasourceService.getList(keyword));
    }


    @GetMapping("/{id}/data")
    @ApiOperation(value="根据数据源获取 sql执行 所获取的数据")
    @ApiImplicitParam(name = "sql",value = "sql",dataType = "string")
    public Response<List<Map<String,Object>>> getDataList(@PathVariable String id, String sql,String field,String keyword) throws Exception {
        return Response.ok(datasourceService.getDataList(id, sql, field, keyword));
    }

    @GetMapping("/{id}/fields")
    @ApiOperation(value="根据数据源获取 sql执行 所获取的字段")
    @ApiImplicitParam(name = "sql", value = "sql", dataType = "string")
    public Response<List<String>> getFields(@PathVariable String id, String sql) throws Exception {
        return Response.ok(datasourceService.getFields(id, sql));
    }

    @GetMapping("/{id}/tree")
    @ApiOperation(value="根据数据源获取 sql执行 所获取的值 转为树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "field", value = "field", dataType = "string"),
            @ApiImplicitParam(name = "parentfield", value = "parentfield", dataType = "parentfield")
    })
    public Response<List<Map<String,Object>>> getDataToTree(@PathVariable String id, String field, String parentfield) throws Exception {
        return Response.ok(datasourceService.getDataToTree(id,field,parentfield));
    }

    @GetMapping("/{id}/columns/{columns}/data")
    @ApiOperation(value="根据数据源获取 sql执行 所获取的某些字段的数据 可以多选 field1,field2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string"),
            @ApiImplicitParam(name = "columns", value = "columns", dataType = "columns")
    })
    public Response<List<Map<String,Object>>> getDataByColumns(@PathVariable String id, @PathVariable String columns) throws Exception {
        return Response.ok(datasourceService.getDataByColumns(id,columns));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    public Response<Boolean> updateDataSource(@PathVariable String id, @RequestBody DataSourceDto dto){
        int count = datasourceService.count(Wrappers.<XjrBaseDatasource>query().lambda().eq(XjrBaseDatasource::getId, id));
        if (count == 0){
            return Response.notOk("此数据源不存在。");
        }
        XjrBaseDatasource datasource = BeanUtil.copy(dto, XjrBaseDatasource.class);
        return Response.ok(datasourceService.updateDatasource(id, datasource));
    }

    @PostMapping
    @ApiOperation(value = "新增")
    public Response<Boolean> addDataSource(@RequestBody DataSourceDto dto){
        XjrBaseDatasource datasource = BeanUtil.copy(dto, XjrBaseDatasource.class);
        return Response.ok(datasourceService.save(datasource) && OrganizationCacheUtil.addCache(OrganizationCacheUtil.DATASOURCE_CACHE_KEY, datasource));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除数据源")
    public Response deleteDataSource(@PathVariable String ids){
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idArray != null && idArray.length == 1) {
            isSuccess = datasourceService.removeById(idArray[0]);
        } else {
            isSuccess = datasourceService.removeByIds(Arrays.asList(idArray));
        }
        if (isSuccess) {
            OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.DATASOURCE_CACHE_KEY, idArray);
        }
        return Response.status(isSuccess);
    }
}
