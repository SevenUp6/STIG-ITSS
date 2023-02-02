package com.xjrsoft.module.excel.controller;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSONObject;
import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.excel.dto.BaseExcelImportDto;
import com.xjrsoft.module.excel.dto.GetListExcelImportDto;
import com.xjrsoft.module.excel.entity.XjrExcelImport;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.factory.ParserFactory;
import com.xjrsoft.module.excel.parser.ImportDataParser;
import com.xjrsoft.module.excel.service.IXjrExcelImportService;
import com.xjrsoft.module.excel.service.IXjrExcelImportfiledsService;
import com.xjrsoft.module.excel.vo.ExcelImportVo;
import com.xjrsoft.module.excel.vo.ExcelImportfiledsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:光华科技-软件研发部
 * @Date:2020/11/11
 * @Description:Excel导入模板 前端控制器
 */
@AllArgsConstructor
@RequestMapping("/excel-import")
@Api(value = "/excel-import", tags = "excel导入模块")
@RestController
public class XjrExcelImportController {

    private IXjrExcelImportService excelImportService;

    private IXjrExcelImportfiledsService excelImportfiledsService;

    /**
     * @author 光华科技-软件研发部
     * @date:2019年10月25日
     * @url: /queryExcelImport
     * @description: 查询excel的导入按钮信息
     */
    @GetMapping()
    @ApiOperation(value = "获取excel导入分页数据")
    public Response queryExcelImport(GetListExcelImportDto dto) {
        return Response.ok(excelImportService.getPageData(dto));
    }

    /**
     * @author 光华科技-软件研发部
     * @date:2019年10月25日
     * @url: /toUpDatePage
     * @description:跳转编辑页面的数据回显
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取导入配置详情")
    public Response toUpdatePage(@PathVariable String id) {
        JSONObject json = new JSONObject();
        XjrExcelImport xjrExcelImport = excelImportService.getById(id);
        if (xjrExcelImport != null) {
            List<XjrExcelImportfileds> excelImportFieldsList = excelImportfiledsService.listByImportId(xjrExcelImport.getId());
            json.put("xjrExcelImport", BeanUtil.copy(xjrExcelImport, ExcelImportVo.class));
            json.put("excelImportFieldsList", BeanUtil.copyList(excelImportFieldsList, ExcelImportfiledsVo.class));
            return Response.ok(json);
        }
        return Response.notOk();
    }

    @PostMapping()
    @ApiOperation(value = "excel导入按钮的新增")
    public Response save(@RequestBody BaseExcelImportDto dto) {
        XjrExcelImport excelImport = BeanUtil.copy(dto.getExcelImport(), XjrExcelImport.class);
        List<XjrExcelImportfileds> excelImportFieldList = BeanUtil.copyList(dto.getImportFieldList(), XjrExcelImportfileds.class);
        return Response.status(excelImportService.saveExcelImport(excelImport, excelImportFieldList));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:更新
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新")
    public Response update(@PathVariable String id, @RequestBody BaseExcelImportDto dto) {
        XjrExcelImport excelImport = BeanUtil.copy(dto.getExcelImport(), XjrExcelImport.class);
        List<XjrExcelImportfileds> excelImportFieldList = BeanUtil.copyList(dto.getImportFieldList(), XjrExcelImportfileds.class);
        return Response.status(excelImportService.updateExcelImport(id, excelImport, excelImportFieldList));
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[]
    * @return:void
    * @Description:删除
    */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public Response deleteExcelImport(@PathVariable String id) {
        return Response.status(excelImportService.delete(id));
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:启动
    */
    @PatchMapping("/start/{id}")
    @ApiOperation(value = "启动")
    public Response start(@PathVariable String id) {
        XjrExcelImport excelImport = new XjrExcelImport();
        excelImport.setEnabledMark(1);
        return Response.status( excelImportService.updateById(excelImport));
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:启动
    */
    @PatchMapping("/stop/{id}")
    @ApiOperation(value = "停止")
    public Response stop(@PathVariable String id) {
        XjrExcelImport excelImport = new XjrExcelImport();
        excelImport.setEnabledMark(0);
        return Response.status(excelImportService.updateById(excelImport));
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[]
    * @return:void
    * @Description:导入Excel
    */
    @SneakyThrows
    @PostMapping("/import")
    @ApiOperation(value = "Excel数据导入")
    public Response importExcel(MultipartFile file, @RequestParam(required = true, value = "F_ModuleId") String moduleId) {
        XjrExcelImport excelImport = excelImportService.getByModuleId(moduleId);
        ImportDataParser parser = ParserFactory.getImportDataParser();
        File excelFile = IoUtil.toFile(file);
        List<Map<String, Object>> recordList = parser.parseDataToMap(excelImport, excelFile);
        if (CollectionUtil.isNotEmpty(recordList)) {
            String tableName = excelImport.getDbTable();
            List<Entity> entityList = recordList.stream().map(record -> {
                Entity entity = Entity.create(tableName);
                entity.putAll(record);
                return entity;
            }).collect(Collectors.toList());
            String dbId = excelImport.getDbId();
            Db.use(DataSourceUtil.getDataSource(dbId)).insert(entityList);
            return Response.ok("导入成功！");
        }
        return Response.notOk("导入失败！");
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[titleDataList:列数据]
    * @return:void
    * @Description:下载excel模板
    */
    @SneakyThrows
    @GetMapping("/download/{moduleId}")
    @ApiOperation(value = "导入模板下载")
    public void downloadExcelModel(@PathVariable  String moduleId) {
        // 列数据
        Map<String, String> titleData = new HashMap<>(16);
        XjrExcelImport excelImport = excelImportService.getByModuleId(moduleId);
        if (excelImport != null) {
            List<XjrExcelImportfileds> fieldsList = excelImportfiledsService.listByImportId(excelImport.getId());
            for (XjrExcelImportfileds excelImportFields : fieldsList) {
                if (excelImportFields.getRelationType() != 1) {
                    String name = excelImportFields.getName();
                    String colName = excelImportFields.getColName();
                    titleData.put(name, colName == null ? name : colName);
                }
            }
        }
        File file = excelImportService.downloadExcelModel(titleData);
        WebUtil.writeFileToResponse(excelImport.getName() + StringPool.DOT + StringUtils.substringAfterLast(file.getName(), StringPool.DOT), file);
    }
}
