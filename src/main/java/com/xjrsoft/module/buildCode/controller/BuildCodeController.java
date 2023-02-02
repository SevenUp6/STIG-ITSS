package com.xjrsoft.module.buildCode.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.xjrsoft.common.dbmodel.DbExecutor;
import com.xjrsoft.common.dbmodel.entity.TableField;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
import com.xjrsoft.common.dbmodel.utils.SqlUtil;
import com.xjrsoft.common.dbmodel.utils.TableInfoUtil;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DbUtil;
import com.xjrsoft.core.tool.utils.*;
//import com.xjrsoft.module.app.entity.XjrAppFunction;
//import com.xjrsoft.module.app.service.IXjrAppFunctionService;
import com.xjrsoft.module.base.entity.XjrBaseDatabaselink;
import com.xjrsoft.module.base.service.IXjrBaseDatabaseLinkService;
import com.xjrsoft.module.base.service.IXjrBaseModuleService;
import com.xjrsoft.module.buildCode.dto.*;
import com.xjrsoft.module.buildCode.grant.AppViewCodeGenerator;
import com.xjrsoft.module.buildCode.grant.XjrCodeGenerator;
import com.xjrsoft.module.buildCode.props.GlobalConfigProperties;
import com.xjrsoft.module.buildCode.utils.BuildCodeUtil;
import com.xjrsoft.module.buildCode.utils.ViewCodeGenUtil;
import com.xjrsoft.module.buildCode.vo.CodePathVo;
import com.xjrsoft.module.excel.entity.XjrExcelImport;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.service.IXjrExcelImportService;
import com.xjrsoft.module.form.entity.XjrFormSchemeInfo;
import com.xjrsoft.module.form.service.IXjrFormSchemeInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 表单-菜单关联关系表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/build-code")
@Api(value = "/build-code", tags = "代码生成器模块")
public class BuildCodeController {

//    private IBuildCodeService buildCodeService;

    private IXjrBaseModuleService moduleService;

    private IXjrBaseDatabaseLinkService databaseLinkService;

    private DbExecutor dbExecutor;

    private GlobalConfigProperties buildCodeProperties;

    private IXjrExcelImportService importService;

    private IXjrFormSchemeInfoService schemeInfoService;

//    private IXjrAppFunctionService appFunctionService;

    @GetMapping("/output-area")
    @ApiOperation(value="获取输出区域")
    public Response<CodePathVo> getOutputPath(){
        String frontProjectPath = "E:\\demo\\xjrVue";
        String javaCodePath = "src/main/java/com/xjrsoft/module/{outputArea}";
        String viewCodePath = frontProjectPath + "/src/views";
        CodePathVo codePathVo = new CodePathVo();
        codePathVo.setControllerArea(javaCodePath + "/controller");
        codePathVo.setModelArea(javaCodePath + "/entity");
        codePathVo.setRepositoryArea(javaCodePath + "/mapper");
        codePathVo.setServiceArea(javaCodePath + "/service");
        return Response.ok(codePathVo);
    }

    @PostMapping("/preview-web-code")
    @ApiOperation(value="获取所需要生成的代码")
    public Response<Map<String, String>> previewCode(@RequestBody CodeSchemaDto codeSchemaDto) {
        XjrCodeGenerator generator = buildGenerator(codeSchemaDto);
        generator.run(codeSchemaDto, false, false);
        return Response.ok(generator.getResultCodeMap());
    }

    @PostMapping("/build-web-code")
    @ApiOperation(value="生成所需要生成的代码")
    public Response genCode(@RequestBody CodeSchemaDto codeSchemaDto) throws Exception {
        String frontProjectPath = buildCodeProperties.getFontProjectPath();
        CodeContentDto codeContentDto = codeSchemaDto.getCodeContentDto();
        BaseInfoDto baseInfoDto = codeSchemaDto.getBaseInfoDto();
        String fontDirectory = baseInfoDto.getFontDirectory();
        File dir = new File(frontProjectPath + "/src/" +fontDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String listHTML = codeContentDto.getListHTML();
        FileOutputStream listFos = new FileOutputStream(frontProjectPath + "/src/" +fontDirectory + "/" + baseInfoDto.getName() + "List.vue");
        IoUtil.write(listFos, true, listHTML.getBytes("UTF-8"));

        String formHTML = codeContentDto.getFormHTML();
        FileOutputStream formFos = new FileOutputStream(frontProjectPath + "/src/" +fontDirectory + "/" + baseInfoDto.getName() + "Form.vue");
        IoUtil.write(formFos, true, formHTML.getBytes("UTF-8"));
        XjrCodeGenerator generator = buildGenerator(codeSchemaDto);
        generator.run(codeSchemaDto, true, false);

        String moduleId = IdWorker.get32UUID();
        // 保存excel导入配置信息
        this.saveImportData(moduleId, codeSchemaDto);
        // 保存系统表单配置信息
        Boolean isWorkflowForm = baseInfoDto.getIsWorkflowForm();
        if (isWorkflowForm != null && isWorkflowForm) {
            this.saveFormData(moduleId, codeSchemaDto);
        }
        return Response.ok(moduleService.saveModule(moduleId, codeSchemaDto));
    }

    @PostMapping("/build-web-code/customtable")
    @ApiOperation(value="(自定义表结构模版) 生成所需要生成的代码")
    public Response buildCodeByCustomTable(@RequestBody CodeSchemaDto codeSchemaDto) throws Exception {
        String frontProjectPath = buildCodeProperties.getFontProjectPath();
        CodeContentDto codeContentDto = codeSchemaDto.getCodeContentDto();
        BaseInfoDto baseInfoDto = codeSchemaDto.getBaseInfoDto();

        XjrCodeGenerator generator = buildGenerator(codeSchemaDto);
        // 添加关联字段
        List<TableInfoDto> tableInfoDtoList = codeSchemaDto.getTableInfoDtoList();
        TableInfoUtil.buildRelationField(codeSchemaDto.getDbTableDtoList(), tableInfoDtoList);
        generator.run(codeSchemaDto, true, true);
        if (BooleanUtils.isTrue(baseInfoDto.getHaveSql())) {
            List<TableInfo> tableInfoList = codeSchemaDto.getTableInfoDtoList().stream().map( tableInfoDto ->{
                TableInfo tableInfo = BeanUtil.copy(tableInfoDto, TableInfo.class);
                tableInfo.setFields(BeanUtil.copyList(tableInfoDto.getFields(), TableField.class));
                // 添加公共字段
                TableInfoUtil.addComFieldAndKey(tableInfo);
                return tableInfo;
            }).collect(Collectors.toList());
            List<String> sqlList = SqlUtil.buildCreateTableSql(generator.getDbType(), tableInfoList);
            for (String sql : sqlList) {
                if (StringUtil.isBlank(sql)) continue;
                dbExecutor.executeUpdate(codeSchemaDto.getDbLinkId(), sql);
            }
        }
        String moduleId = IdWorker.get32UUID();
        // 保存excel导入配置信息
        this.saveImportData(moduleId, codeSchemaDto);
        // 保存系统表单配置信息
        Boolean isWorkflowForm = baseInfoDto.getIsWorkflowForm();
        if (isWorkflowForm != null && isWorkflowForm) {
            this.saveFormData(moduleId, codeSchemaDto);
        }
        // 生成前端代码
        String fontDirectory = baseInfoDto.getFontDirectory();
        File dir = new File(frontProjectPath + "/src/" +fontDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String listHTML = codeContentDto.getListHTML();
        FileOutputStream listFos = new FileOutputStream(frontProjectPath + "/src/" +fontDirectory + "/" + baseInfoDto.getName() + "List.vue");
        IoUtil.write(listFos, true, listHTML.getBytes("UTF-8"));

        String formHTML = codeContentDto.getFormHTML();
        FileOutputStream formFos = new FileOutputStream(frontProjectPath + "/src/" +fontDirectory + "/" + baseInfoDto.getName() + "Form.vue");
        IoUtil.write(formFos, true, formHTML.getBytes("UTF-8"));
        return Response.ok(moduleService.saveModule(moduleId, codeSchemaDto));
    }



    @PostMapping("/preview-app-function-code")
    @ApiOperation(value="预览所需要生成的移动端代码")
    public Response previewAppCode(@RequestBody AppCodeSchemaDto appCodeSchemaDto) {
        CodeSchemaDto codeSchemaDto = BuildCodeUtil.transAppParamToWeb(appCodeSchemaDto);
        XjrCodeGenerator generator = buildGenerator(codeSchemaDto);
        BaseInfoDto baseInfoDto = appCodeSchemaDto.getBaseInfoDto();
        generator.setIsWebModule(false);
        generator.setPackageName("com.xjrsoft.module.customerApp." + baseInfoDto.getOutputArea().get("F_ItemValue") + "." + StringUtil.lowerFirst(baseInfoDto.getName()));
        generator.run(codeSchemaDto, false, false);
        Map<String, String> resultCodeMap = generator.getResultCodeMap();
        // 找到主键
        String mainPk = StringPool.EMPTY;
        List<DbTableDto> dbTableDtoList = appCodeSchemaDto.getDbSettingDto().getDbTableDtoList();
        for (DbTableDto dbTableDto : dbTableDtoList) {
            if (dbTableDto.isMainTable()) {
                mainPk = dbTableDto.getPk();
            }
        }
        String listViewCode = AppViewCodeGenerator.genListViewCode(mainPk, appCodeSchemaDto.getListViewSettingDto(), appCodeSchemaDto.getQueryFieldDtoList(), appCodeSchemaDto.getFormDataDtoList(), appCodeSchemaDto.getBaseInfoDto());
        resultCodeMap.put("listHTML", listViewCode);
        String formViewCode = AppViewCodeGenerator.genFormViewCode(dbTableDtoList, appCodeSchemaDto.getFormDataDtoList(), appCodeSchemaDto.getBaseInfoDto());
        resultCodeMap.put("formHTML", formViewCode);
        return Response.ok(resultCodeMap);
    }

    @PostMapping("/build-app-function-code")
    @ApiOperation(value="生成所需要生成的移动端代码")
    public Response buildAppCode(@RequestBody AppCodeSchemaDto appCodeSchemaDto) throws Exception {
        CodeSchemaDto codeSchemaDto = BuildCodeUtil.transAppParamToWeb(appCodeSchemaDto);
        BaseInfoDto baseInfoDto = appCodeSchemaDto.getBaseInfoDto();
        XjrCodeGenerator generator = buildGenerator(codeSchemaDto);
        generator.setIsWebModule(false);
        generator.setPackageName("com.xjrsoft.module.customerApp." + baseInfoDto.getOutputArea().get("F_ItemValue") + "." + StringUtil.lowerFirst(baseInfoDto.getName()));
        generator.run(codeSchemaDto, true, false);
        List<AppFieldConfigDto> formDataDtoList = appCodeSchemaDto.getFormDataDtoList();
        String appProjectPath = buildCodeProperties.getAppProjectPath();
        String fontDirectory = "pages/basics/customercode/";
        String outputArea = baseInfoDto.getOutputArea().get("F_ItemValue" );
        String appModuleName = baseInfoDto.getName();
        String dirPath = appProjectPath + File.separator + fontDirectory + outputArea;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String mainPk = StringPool.EMPTY;
        List<DbTableDto> dbTableDtoList = appCodeSchemaDto.getDbSettingDto().getDbTableDtoList();
        for (DbTableDto dbTableDto : dbTableDtoList) {
            if (dbTableDto.isMainTable()) {
                mainPk = dbTableDto.getPk();
            }
        }
        String listViewCode = AppViewCodeGenerator.genListViewCode(mainPk, appCodeSchemaDto.getListViewSettingDto(), appCodeSchemaDto.getQueryFieldDtoList(), formDataDtoList, baseInfoDto);
        FileOutputStream listFos = new FileOutputStream(dirPath + File.separator + appModuleName + "List.vue");
        IoUtil.write(listFos, true, listViewCode.getBytes("UTF-8"));
        String formViewCode = AppViewCodeGenerator.genFormViewCode(appCodeSchemaDto.getDbSettingDto().getDbTableDtoList(), formDataDtoList, baseInfoDto);
        FileOutputStream formFos = new FileOutputStream(dirPath + File.separator + appModuleName + "Form.vue");
        IoUtil.write(formFos, true, formViewCode.getBytes("UTF-8"));

        // 菜单信息保存
//        AppModuleInfoDto appModuleInfoDto = appCodeSchemaDto.getAppModuleInfoDto();
//        XjrAppFunction appFunction = BeanUtil.copy(appModuleInfoDto, XjrAppFunction.class);
//        appFunction.setUrl("customercode/" + outputArea + "/" + appModuleName + "List");
//        appFunctionService.save(appFunction);

        // 修改app配置文件
        String pageJsonPath = buildCodeProperties.getAppProjectPath() + File.separator + "pages.json";
        String pageJsonText = IoUtil.read(new FileInputStream(pageJsonPath), "UTF-8" );
        JSONObject pageJson = JSONObject.parseObject(pageJsonText);
        JSONArray pages = pageJson.getJSONArray("pages" );
        JSONObject json = (JSONObject) pages.get(pages.size() - 1);
        JSONObject listJson = new JSONObject();
        listJson.putAll(json);
        listJson.put("path", fontDirectory + outputArea + "/" + appModuleName + "List");
        pages.add(listJson);

        JSONObject formJson = new JSONObject();
        formJson.putAll(json);
        formJson.put("path", fontDirectory + outputArea + "/" + appModuleName + "Form");
        pages.add(formJson);

        String pageJsonStr = JSON.toJSONString(pageJson, SerializerFeature.DisableCircularReferenceDetect);
        com.xjrsoft.core.tool.utils.IoUtil.write(JsonFormatUtil.formatJson(pageJsonStr), new FileOutputStream(pageJsonPath), Charset.forName("UTF-8"));

        return Response.ok();
    }

    @PostMapping("/preview-web-code/customtable")
    @ApiOperation(value="(自定义表结构模版) 生成所需要生成的代码")
    public Response previewCodeByCustomTable(@RequestBody CodeSchemaDto codeSchemaDto) {
        BaseInfoDto baseInfoDto = codeSchemaDto.getBaseInfoDto();
        XjrCodeGenerator generator = buildGenerator(codeSchemaDto);
        // 添加关联字段
        List<TableInfoDto> tableInfoDtoList = codeSchemaDto.getTableInfoDtoList();
        TableInfoUtil.buildRelationField(codeSchemaDto.getDbTableDtoList(), tableInfoDtoList);
        generator.run(codeSchemaDto, false, true);
        Map<String, String> resultCodeMap = generator.getResultCodeMap();
        Boolean haveSql = baseInfoDto.getHaveSql();
        if (haveSql != null && haveSql) {
            StringBuilder sqls = new StringBuilder();
            List<TableInfo> tableInfoList = tableInfoDtoList.stream().map(tableInfoDto ->{
                TableInfo tableInfo = BeanUtil.copy(tableInfoDto, TableInfo.class);
                tableInfo.setFields(BeanUtil.copyList(tableInfoDto.getFields(), TableField.class));
                // 添加公共字段
                TableInfoUtil.addComFieldAndKey(tableInfo);
                return tableInfo;
            }).collect(Collectors.toList());
            List<String> sqlList = SqlUtil.buildCreateTableSql(generator.getDbType(), tableInfoList);
            if (CollectionUtil.isNotEmpty(sqlList)) {
                sqlList.stream().forEach(sql -> sqls.append(sql).append(System.lineSeparator()));
            }
            resultCodeMap.put("dbSqlCode", sqls.toString());
        }
        return Response.ok(resultCodeMap);
    }

    @SneakyThrows
    private XjrCodeGenerator buildGenerator(CodeSchemaDto codeSchemaDto) {
        String dbLinkId = codeSchemaDto.getDbLinkId();
        BaseInfoDto baseInfoDto = codeSchemaDto.getBaseInfoDto();
        String serviceName = baseInfoDto.getName();
        String outputArea = baseInfoDto.getOutputArea().get("F_ItemValue");
        XjrCodeGenerator generator = new XjrCodeGenerator();
        // 设置数据源
        DatabaseMetaData metaData = DataSourceUtil.getDataSource(dbLinkId).getConnection().getMetaData();
        DbType dbType = JdbcUtils.getDbType(metaData.getURL());
        if (StringUtil.isNotBlank(dbLinkId) && !StringUtils.equalsIgnoreCase(dbLinkId, "localDB")) {
            XjrBaseDatabaselink databaseLink = databaseLinkService.getById(dbLinkId);
            generator.setDriverName(DbUtil.getDriverByType(databaseLink.getDbType()));
            generator.setUrl(databaseLink.getDbConnection());
            generator.setUsername(databaseLink.getDbUserName());
            generator.setPassword(databaseLink.getDbPwd());
        }
        generator.setDbType(dbType);
        // 设置基础配置
        generator.setServiceName(serviceName);
        generator.setPackageName("com.xjrsoft.module." + outputArea + "." + StringUtil.lowerFirst(serviceName));
//        generator.setPackageDir("E:\\xjrsoft-developer-kit-8.0.26\\eclipseWorkSpace\\xjrsoft_new");
//        generator.setPackageWebDir("E:\\demo\\saber");
//        generator.setTablePrefix(Func.toStrArray("f_"));
//        generator.setIncludeTables(Func.toStrArray("f_parent"));
        // 设置是否继承基础业务字段
        generator.setHasSuperEntity(false);
        // 设置是否开启包装器模式
        generator.setHasWrapper(false);
        return generator;
    }

    /**
     * 保存excel导入配置信息
     * @param moduleId
     * @param codeSchemaDto
     * @return
     */
    private boolean saveImportData(String moduleId, CodeSchemaDto codeSchemaDto) {
        ImportDataDto importDataDto = codeSchemaDto.getColDataDto().getImportDataDto();
        if (importDataDto == null || !CollectionUtil.isNotEmpty(importDataDto.getImportFieldsDtoList())) {
            return false;
        }
        XjrExcelImport excelImport = new XjrExcelImport();
        excelImport.setBtnName(importDataDto.getBindBtn());
        excelImport.setName(importDataDto.getModuleName());
        excelImport.setModuleId(moduleId);
        List<XjrExcelImportfileds> importFieldList = BeanUtil.copyList(importDataDto.getImportFieldsDtoList(), XjrExcelImportfileds.class);
        return importService.saveExcelImport(excelImport, importFieldList);
    }

    private boolean saveFormData(String moduleId, CodeSchemaDto codeSchemaDto) {
        ModuleDataDto moduleDataDto = codeSchemaDto.getModuleDataDto();
        XjrFormSchemeInfo schemeInfo = new XjrFormSchemeInfo();
        schemeInfo.setName(moduleDataDto.getFullName());
        schemeInfo.setType(2);
        schemeInfo.setCategory("11");// 表单分类，默认“其他”
        schemeInfo.setSchemeId(moduleId);
        return schemeInfoService.save(schemeInfo);
//        List<FormDataDto> formDataDtoList = codeSchemaDto.getFormDataDtoList();
//        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
//        String fieldSettingJson = StringPool.EMPTY;
//        String dbTablesJson = StringPool.EMPTY;
//        try {
//            fieldSettingJson = objectMapper.writeValueAsString(formDataDtoList);
//            dbTablesJson = objectMapper.writeValueAsString(codeSchemaDto.getDbTableDtoList());
//        } catch (JsonProcessingException e) {
//            log.error("转换表单json异常！", e);
//        }
//        JSONArray fieldsJson = JSON.parseArray(fieldSettingJson);
//        JSONObject formDataJson = new JSONObject();
//        JSONObject dataJson = new JSONObject();
//        dataJson.put("fields", fieldsJson);
//        formDataJson.put("data", dataJson);
//        formDataJson.put("dbLinkId", codeSchemaDto.getDbLinkId());
//        formDataJson.put("dbTable", JSON.parseArray(dbTablesJson));
//        XjrFormScheme scheme = new XjrFormScheme();
//        scheme.setScheme(formDataJson.toJSONString());
//        scheme.setType(0);
//        return schemeInfoService.addFormScheme(scheme, schemeInfo);
    }

    @PostMapping("/preview-app-login-code")
    @ApiOperation(value="预览APP登录页所需要生成的代码")
    public Response previewLoginViewCode(@RequestBody Map<String, Object> params) {
        Map<String, Object> config = MapUtils.getMap(params, "config");
        String loginViewCode = ViewCodeGenUtil.genAppLoginViewCode(config);
        return Response.ok(loginViewCode);
    }

    @PostMapping("/build-app-login-code")
    @ApiOperation(value="生成APP登录页所需要生成的代码")
    public Response genLoginViewCode(@RequestBody Map<String, Object> params) {
        String loginViewCode = MapUtils.getString(params, "code");
        String loginFilePath = buildCodeProperties.getAppProjectPath() + File.separator + ViewCodeGenUtil.LOGIN_FOLDER_PATH + File.separator + "login.vue";
        try {
            // 备份原有登录文件
            File oldLoginFile = new File(loginFilePath);
            FileUtil.rename(oldLoginFile, "login_" + System.currentTimeMillis() + ".vue", true);
            // 生成新的登录文件
            FileUtil.writeString(loginViewCode, loginFilePath, StringPool.UTF_8);
        } catch (Exception e) {
            log.error("生成登录页失败！", e);
            return Response.notOk(e.getMessage());
        }
        return Response.ok();
    }

    @PostMapping("/preview-app-index-code")
    @ApiOperation(value="预览APP首页所需要生成的代码")
    public Response previewIndexViewCode(@RequestBody Map<String, Object> params) {
        List<Map<String, Object>> viewSettings = (List<Map<String, Object>>) params.get("stepTwo");
        String viewCode = ViewCodeGenUtil.genAppIndexViewCode(viewSettings);
        return Response.ok(viewCode);
    }

    @PostMapping("/build-app-index-code")
    @ApiOperation(value="生成APP首页所需要生成的代码")
    public Response genIndexViewCode(@RequestBody Map<String, Object> params) {
        List<Map<String, Object>> viewSettings = (List<Map<String, Object>>) params.get("stepTwo");
        String viewCode = ViewCodeGenUtil.genAppIndexViewCode(viewSettings);
        String indexFilePath = buildCodeProperties.getAppProjectPath() + File.separator + ViewCodeGenUtil.INDEX_FOLDER_PATH + File.separator + "index.vue";
        try {
            // 备份原有登录文件
            File oldLoginFile = new File(indexFilePath);
            FileUtil.rename(oldLoginFile, "index_" + System.currentTimeMillis() + ".vue", true);
            // 生成新的登录文件
            FileUtil.writeString(viewCode, indexFilePath, StringPool.UTF_8);
        } catch (Exception e) {
            log.error("生成首页失败！", e);
            return Response.notOk(e.getMessage());
        }
        return Response.ok();
    }
}
