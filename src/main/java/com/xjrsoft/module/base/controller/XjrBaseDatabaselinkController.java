package com.xjrsoft.module.base.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.Enum.DeleteMarkEnum;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.dbmodel.DbExecutor;
import com.xjrsoft.common.dbmodel.DbTableInfo;
import com.xjrsoft.common.dbmodel.TableColumnInfo;
import com.xjrsoft.common.dbmodel.entity.TableField;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.DatabaselinkDto;
import com.xjrsoft.module.base.dto.DbTableListDto;
import com.xjrsoft.module.base.dto.ReleaseTableDto;
import com.xjrsoft.module.base.dto.TestConnectionDto;
import com.xjrsoft.module.base.entity.XjrBaseDatabaselink;
import com.xjrsoft.module.base.service.IXjrBaseDatabaseLinkService;
import com.xjrsoft.module.base.vo.DatabaselinkVo;
import com.xjrsoft.module.base.vo.SqlTreeDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据库连接表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Slf4j
@RestController
@RequestMapping("/database-links")
@Api(value = "/database-links",tags = "数据库连接模块")
@AllArgsConstructor
public class XjrBaseDatabaselinkController {

    private final IXjrBaseDatabaseLinkService databaselinkService;

    private DbExecutor executor;

    @GetMapping("/{id}")
    @ApiOperation(value="获取数据库链接详情")
    @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "string")
    public Response<DatabaselinkVo> getById(@PathVariable String id){
        return Response.ok(BeanUtil.copy(databaselinkService.getById(id), DatabaselinkVo.class));
    }

    @GetMapping
    @ApiOperation(value="查询数据库列表-不分页")
    @ApiImplicitParam(name = "keyword",value = "keyword",dataType = "string")
    public Response<List<DatabaselinkVo>> getList(String keyword){
        List<XjrBaseDatabaselink> list = databaselinkService.list(Wrappers.<XjrBaseDatabaselink>query().lambda()
                .select(XjrBaseDatabaselink.class,x -> VoToColumn.Convert(DatabaselinkVo.class).contains(x.getColumn()))
                .like(StringUtil.isNotBlank(keyword), XjrBaseDatabaselink::getDbName, keyword)
                .eq(XjrBaseDatabaselink::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()));
        return Response.ok(BeanUtil.copyList(list, DatabaselinkVo.class));
    }


    @GetMapping("/group")
    @ApiOperation(value="查询数据库列表-分组")
    public Response<Map<String,List<DatabaselinkVo>>> getListGroup() {
        List<XjrBaseDatabaselink> list = databaselinkService.list(Wrappers.<XjrBaseDatabaselink>query().lambda().eq(XjrBaseDatabaselink::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()));
        List<DatabaselinkVo> voList = BeanUtil.copyList(list, DatabaselinkVo.class);
        Map<String, List<DatabaselinkVo>> result = voList.stream()
                .collect(Collectors.groupingBy(DatabaselinkVo::getServerAddress));
        return Response.ok(result);
    }

    @GetMapping("/test")
    @ApiOperation(value="测试数据库连接")
    @ApiImplicitParam(name = "dto",value = "dto",dataType = "TestConnectionDto")
    public Response<Boolean> testConnection(TestConnectionDto dto) throws SQLException, ClassNotFoundException {
        return Response.ok(databaselinkService.testConnection(dto));
    }

    @GetMapping("/{id}/tables")
    @ApiOperation(value="根据数据库连接Id 获取所有表信息")
    public Response<List<DbTableInfo>> getTablesById(@PathVariable String id, DbTableListDto dto) throws Exception {
        return Response.ok(databaselinkService.getTablesById(id, dto.getTableName(), null));
    }
    @GetMapping("/{id}/tables/page")
    @ApiOperation(value="根据数据库连接Id 获取所有表信息")
    public Response<PageOutput<DbTableInfo>> getPageTablesById(@PathVariable String id, DbTableListDto dto) throws Exception {
        IPage<DbTableInfo> page = ConventPage.getPage(dto);
        List<DbTableInfo> resultList = databaselinkService.getTablesById(id, dto.getTableName(), page);
        return Response.ok(ConventPage.getPageOutput(page.getTotal(), resultList));
    }

    @GetMapping("/{id}/datas")
    @ApiOperation(value="根据数据库连接Id  执行sql  获取所有数据")
    public Response<List<Map<String,Object>>> getDataById(@PathVariable String id, @RequestParam String sql) throws Exception {
        return Response.ok(databaselinkService.getDataById(id,sql));
    }


    @GetMapping("/{id}/fields")
    @ApiOperation(value="根据数据库连接Id  执行sql  获取所有列名")
    public Response<List<String>> getFieldsById(@PathVariable String id, @RequestParam String sql) throws Exception {
        return Response.ok(databaselinkService.getFieldById(id,sql));
    }


    @GetMapping("/{id}/tables/{tableName}/columns")
    @ApiOperation(value="根据数据库连接Id 以及表名 查询所有列")
    public Response<List<TableColumnInfo>> getFieldByIdAndTableName(@PathVariable String id, @PathVariable String tableName) throws Exception {
        return Response.ok(databaselinkService.getFieldByIdAndTableName(id,tableName));
    }

    @GetMapping("/{id}/tables/{tableName}/datas")
    @ApiOperation(value="根据数据库连接Id 以及表名 查询所有数据")
    public Response<List<Map<String,Object>>> getDataByIdAndTableName(@PathVariable String id,@PathVariable String tableName) throws Exception {
        return Response.ok(databaselinkService.getDataByIdAndTableName(id,tableName));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改")
    public Response updateDatabaseLink(@PathVariable String id, @RequestBody DatabaselinkDto dto){
        XjrBaseDatabaselink dbLink = BeanUtil.copy(dto, XjrBaseDatabaselink.class);
        int count = databaselinkService.count(Wrappers.<XjrBaseDatabaselink>query().lambda().eq(XjrBaseDatabaselink::getDatabaseLinkId, id));
        if (count == 0){
            return Response.notOk("此连接不存在。");
        }
        return Response.ok(databaselinkService.updateDatabaseLink(id, dbLink));
    }

    @PostMapping
    @ApiOperation(value="新增")
    public Response<Boolean> addDatabaseLink(@RequestBody DatabaselinkDto dto){
        XjrBaseDatabaselink dbLink = BeanUtil.copy(dto, XjrBaseDatabaselink.class);
        return Response.ok(databaselinkService.addDatabaseLink(dbLink));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除")
    public Response<Boolean> deleteDatabaseLink(@PathVariable String ids){
        return Response.ok(databaselinkService.deleteDatabaseLink(ids));
    }

    @GetMapping("/{id}/tree")
    @ApiOperation(value="根据数据库链接Id Sql field parentfield 获取树结构")
    public Response getTreeDataBySql(@PathVariable String id, @RequestParam(required = false) String field, @RequestParam("parentfield") String parentField, String sql) {
        try {
            List<Map<String, Object>> mapList = executor.executeQuery(id, sql);
            List<SqlTreeDataVo> dataList = mapList.stream().map(map -> {
                SqlTreeDataVo sqlTreeDataVo = new SqlTreeDataVo();
                sqlTreeDataVo.putAll(map);
                sqlTreeDataVo.setIdName(field);
                sqlTreeDataVo.setParentIdName(parentField);
                return sqlTreeDataVo;
            }).collect(Collectors.toList());
            return Response.ok(ForestNodeMerger.merge(dataList));
        } catch (SQLException e) {
            return Response.notOk("执行sql失败！");
        }
    }

    @PostMapping("/release")
    @ApiOperation(value="创建数据库表")
    public Response createTable(@RequestBody ReleaseTableDto releaseTableDto) {
        String dbLinkId = releaseTableDto.getDbLinkId();
        TableInfo tableInfo = BeanUtil.copy(releaseTableDto, TableInfo.class);
        tableInfo.setFields(BeanUtil.copyList(releaseTableDto.getFields(), TableField.class));
        return Response.status(databaselinkService.createTable(dbLinkId, tableInfo));
    }

    @GetMapping("/{id}/columns/{columns}/data")
    @ApiOperation(value="执行sql获取的某些字段的数据 可以多选 field1,field2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string"),
            @ApiImplicitParam(name = "columns", value = "columns", dataType = "columns")
    })
    public Response<List<Map<String,Object>>> getDataByColumns(@PathVariable String id, @PathVariable String columns, @RequestParam String sql) {
        return Response.ok(databaselinkService.getDataByColumns(id, columns, sql));
    }

    @GetMapping("/{id}/validate-tablename")
    public Response checkTableName(@PathVariable String id, @RequestParam String tableNames) {
        String[] tableNameArray = StringUtils.split(tableNames, StringPool.COMMA);
        List<String> existTables = databaselinkService.checkTableNames(id, Arrays.asList(tableNameArray));
        if (existTables.size() > 0) {
            return Response.notOk("表名重复，请重新输入：" + existTables);
        }
        return Response.ok();
    }
}
