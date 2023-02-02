package com.xjrsoft.module.form.service.impl;

import cn.hutool.core.util.PageUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;
import cn.hutool.db.sql.Condition;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.xjrsoft.common.dbmodel.DbExecutor;
import com.xjrsoft.common.dbmodel.SqlParam;
import com.xjrsoft.common.dbmodel.entity.TableField;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
import com.xjrsoft.common.dbmodel.utils.SqlUtil;
import com.xjrsoft.common.dbmodel.utils.TableInfoUtil;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.service.IXjrBaseCodeRuleService;
import com.xjrsoft.module.base.service.IXjrBaseDatabaseLinkService;
import com.xjrsoft.module.excel.service.IXjrExcelImportService;
import com.xjrsoft.module.excel.service.IXjrExcelImportfiledsService;
import com.xjrsoft.module.form.constant.FormConstants;
import com.xjrsoft.module.form.dto.FormSchemeDto;
import com.xjrsoft.module.form.dto.FormSchemePageListDto;
import com.xjrsoft.module.form.dto.TableFieldDto;
import com.xjrsoft.module.form.dto.TableInfoDto;
import com.xjrsoft.module.form.entity.XjrFormScheme;
import com.xjrsoft.module.form.entity.XjrFormSchemeInfo;
import com.xjrsoft.module.form.mapper.XjrFormSchemeInfoMapper;
import com.xjrsoft.module.form.service.IXjrFormSchemeInfoService;
import com.xjrsoft.module.form.service.IXjrFormSchemeService;
import com.xjrsoft.module.form.util.FormDataUtil;
import com.xjrsoft.module.form.util.FormSqlUtil;
import com.xjrsoft.module.form.vo.FormSchemeInfoVo;
import com.xjrsoft.module.form.vo.SystemFormVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义表单信息表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Slf4j
@Service
@AllArgsConstructor
public class XjrFormSchemeInfoServiceImpl extends ServiceImpl<XjrFormSchemeInfoMapper, XjrFormSchemeInfo> implements IXjrFormSchemeInfoService {

    private IXjrFormSchemeService schemeService;

//    private IXjrFormRelationService formRelationService;

//    private IXjrBaseDataItemDetailService itemDetailService;

    private IXjrBaseDatabaseLinkService dbLinkService;

    private DbExecutor dbExecutor;

    private IXjrExcelImportService excelImportService;

    private IXjrExcelImportfiledsService excelImportFieldsService;

    private IXjrBaseCodeRuleService codeRuleService;

    @Override
    public boolean addFormScheme(XjrFormScheme scheme, XjrFormSchemeInfo schemeInfo) {
        String schemeInfoId = StringUtil.randomUUID();
        String schemeId = StringUtil.randomUUID();
        schemeInfo.setId(schemeInfoId);
        schemeInfo.setSchemeId(schemeId);
        scheme.setId(schemeId);
        scheme.setSchemeInfoId(schemeInfoId);
        return this.save(schemeInfo) && schemeService.save(scheme);
    }

    @Override
    public FormSchemeDto setDefauleValue(FormSchemeDto formSchemeDto) {
        String scheme = formSchemeDto.getSchemeDto().getScheme();
        List<TableInfoDto> tableInfoDtoList = formSchemeDto.getTableInfoDtoList();
        if (StringUtil.isNotBlank(scheme)) {
            JSONObject schemeObject = JSONObject.parseObject(scheme);
            JSONObject data = schemeObject.getJSONObject("data");
            JSONArray fieldsArray = data.getJSONArray("fields");
            for (int i = 0; i < fieldsArray.size(); i++) {
                JSONObject jsonObject = fieldsArray.getJSONObject(i);
                JSONObject config = jsonObject.getJSONObject("__config__");
                String type = jsonObject.getString("__type__");
                String avueType = config.getString("avueType");
                String bindTableField = config.getString("bindTableField");
                String bindTable = config.getString("bindTable");
                if (StringUtil.equalsIgnoreCase(avueType, "textarea") || StringUtil.equalsIgnoreCase(avueType, "ueditor") || StringUtil.equalsIgnoreCase(avueType, "autoCode") || StringUtil.equalsIgnoreCase(avueType, "upload")) {
                    //多行文本、编辑器、编码、上传、意见簿
                    for (TableInfoDto tableInfoDto : tableInfoDtoList) {
                        if (StringUtil.equalsIgnoreCase(tableInfoDto.getName(), bindTable)) {
                            List<TableFieldDto> fields = tableInfoDto.getFields();
                            if (CollectionUtil.isNotEmpty(fields)) {
                                for (TableFieldDto field : fields) {
                                    if (StringUtil.equalsIgnoreCase(field.getName(), bindTableField)) {
                                        config.put("fieldLength", 2000);
                                        field.setLength(2000);
                                        jsonObject.put("__config__", config);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                } else if (StringUtil.equalsIgnoreCase(type, "user")) {
                    //人员组件
                    for (TableInfoDto tableInfoDto : tableInfoDtoList) {
                        if (StringUtil.equalsIgnoreCase(tableInfoDto.getName(), bindTable)) {
                            List<TableFieldDto> fields = tableInfoDto.getFields();
                            if (CollectionUtil.isNotEmpty(fields)) {
                                for (TableFieldDto field : fields) {
                                    if (StringUtil.equalsIgnoreCase(field.getName(), bindTableField)) {
                                        config.put("fieldLength", 4000);
                                        field.setLength(4000);
                                        jsonObject.put("__config__", config);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else if (StringUtil.equalsIgnoreCase(type, "department")) {
                    //部门组件
                    for (TableInfoDto tableInfoDto : tableInfoDtoList) {
                        if (StringUtil.equalsIgnoreCase(tableInfoDto.getName(), bindTable)) {
                            List<TableFieldDto> fields = tableInfoDto.getFields();
                            if (CollectionUtil.isNotEmpty(fields)) {
                                for (TableFieldDto field : fields) {
                                    if (StringUtil.equalsIgnoreCase(field.getName(), bindTableField)) {
                                        config.put("fieldLength", 500);
                                        field.setLength(500);
                                        jsonObject.put("__config__", config);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //其他组件
                    for (TableInfoDto tableInfoDto : tableInfoDtoList) {
                        if (StringUtil.equalsIgnoreCase(tableInfoDto.getName(), bindTable)) {
                            List<TableFieldDto> fields = tableInfoDto.getFields();
                            if (CollectionUtil.isNotEmpty(fields)) {
                                for (TableFieldDto field : fields) {
                                    if (StringUtil.equalsIgnoreCase(field.getName(), bindTableField)) {
                                        config.put("fieldLength", 200);
                                        field.setLength(200);
                                        jsonObject.put("__config__", config);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            data.put("fields", fieldsArray);
            schemeObject.put("data", data);
            formSchemeDto.getSchemeDto().setScheme(JSONObject.toJSONString(schemeObject));
        }
        return formSchemeDto;
    }

    @Override
    public PageOutput<FormSchemeInfoVo> getPageList(FormSchemePageListDto pageListDto) {
        String keyword = pageListDto.getKeyword();
        IPage<XjrFormSchemeInfo> page = ConventPage.getPage(pageListDto);
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        if (StringUtil.isEmpty(pageListDto.getOrderfield())) {
            page.orders().add(OrderItem.desc("si.F_CreateDate"));
        }
        List<FormSchemeInfoVo> userVoList = this.baseMapper.getPageList(pageListDto.getCategory(), keyword, page);
        return ConventPage.getPageOutput(page.getTotal(), userVoList);
    }

    @Override
    public boolean updateFormScheme(String schemeInfoId, XjrFormScheme scheme, XjrFormSchemeInfo schemeInfo) {
        schemeInfo.setId(schemeInfoId);
        scheme.setSchemeInfoId(schemeInfoId);
//        Integer saveType = scheme.getType();
//        if (saveType == 1) {
        String schemeId = StringUtil.randomUUID();
        schemeInfo.setSchemeId(schemeId);
        scheme.setId(schemeId);
//        } else {
//            XjrFormSchemeInfo oldSchemeInfo = this.getById(schemeInfoId);
//            scheme.setId(oldSchemeInfo.getSchemeId());
//        }
        return this.updateById(schemeInfo) && schemeService.saveOrUpdate(scheme);
    }

    @Override
    public Map<String, Object> getCustomFormData(String formId, String recordId) {
        JSONObject schemeJson = getSchemeJsonByFormId(formId);
        Map<String, Object> resultMap = new HashMap<>();
        if(schemeJson!=null) {
            String dbLinkId = schemeJson.getString(FormConstants.DB_LINK_ID);
            JSONArray dbTables = schemeJson.getJSONArray(FormConstants.DB_TABLE);
            JSONObject data = schemeJson.getJSONObject(FormConstants.DATA);
            JSONArray fields = data.getJSONArray(FormConstants.FIELDS);
            Map<String, List<String>> tableFieldMap = new HashMap<>(dbTables.size());
            for (Object fieldObj : fields) {
                JSONObject fieldJson = (JSONObject) fieldObj;
                JSONObject config = fieldJson.getJSONObject(FormConstants.CONFIG);
                String componentName = MapUtils.getString(config, "componentName");
                if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                    JSONObject tabsMap = config.getJSONObject("childrenObj");
                    for (Object tabObj : tabsMap.values()) {
                        JSONArray tabComponents = (JSONArray) tabObj;
                        for (Object tabComponentObj : tabComponents) {
                            JSONObject tabComponent = (JSONObject) tabComponentObj;
                            JSONObject tabFieldConfig = tabComponent.getJSONObject(FormConstants.CONFIG);
                            buildSelectedFields(tableFieldMap, tabFieldConfig);
                        }
                    }
                } else {
                    buildSelectedFields(tableFieldMap, config);
                }
            }
            // 查询数据
            resultMap = new HashMap<>(dbTables.size());
            for (Object tableObj : dbTables) {
                JSONObject dbTable = (JSONObject) tableObj;
                Map<String, Object> conditionMap = new HashMap<>(1);
                boolean isMainTable = StringUtil.isBlank(dbTable.getString(FormConstants.DB_TABLE_RELATION_NAME));
                if (isMainTable) {
                    conditionMap.put(dbTable.getString(FormConstants.DB_TABLE_PK), recordId);
                } else {
                    conditionMap.put(dbTable.getString(FormConstants.DB_TABLE_FIELD), recordId);
                }
                String tableName = dbTable.getString(FormConstants.DB_TABLE_NAME);
                List<String> columnNameList = tableFieldMap.get(tableName);
                // 添加主键返回
                columnNameList.add(dbTable.getString(FormConstants.DB_TABLE_PK));
                SqlParam sqlParam = FormSqlUtil.buildSimpleSelectFormSqlParam(tableName, columnNameList, conditionMap);
                try {
                    List<Map<String, Object>> resultList = dbExecutor.executeQuery(dbLinkId, sqlParam.getSql(), sqlParam.getPara());
                    resultMap.put(tableName, isMainTable ? (CollectionUtil.isNotEmpty(resultList) ? resultList.get(0) : null) : resultList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }

    private void buildSelectedFields(Map<String, List<String>> tableFieldMap, JSONObject config) {
        Object childrenObj = config.get(FormConstants.CHILDREN);
        // 表格
        if (childrenObj != null) {
            JSONArray childrenFields = (JSONArray) childrenObj;
            for (Object childrenFieldObj : childrenFields) {
                JSONObject childrenField = (JSONObject) childrenFieldObj;
                JSONObject childrenConfig = childrenField.getJSONObject(FormConstants.CONFIG);
                String bindTable = childrenConfig.getString(FormConstants.BIND_TABLE);
                List<String> tableFields = tableFieldMap.get(bindTable);
                if (CollectionUtil.isEmpty(tableFields)) {
                    tableFields = new ArrayList<>();
                    tableFieldMap.put(bindTable, tableFields);
                }
                tableFields.add(childrenConfig.getString(FormConstants.BIND_TABLE_FIELD));
            }
        } else {
            String bindTable = config.getString(FormConstants.BIND_TABLE);
            List<String> tableFields = tableFieldMap.get(bindTable);
            if (CollectionUtil.isEmpty(tableFields)) {
                tableFields = new ArrayList<>();
                tableFieldMap.put(bindTable, tableFields);
            }
            tableFields.add(config.getString(FormConstants.BIND_TABLE_FIELD));
        }
    }

    @Override
    public String addCustomFormData(String formId, Map<String, Object> data) throws Exception {
        JSONObject schemeJson = getSchemeJsonByFormId(formId);
        String dbLinkId = schemeJson.getString(FormConstants.DB_LINK_ID);
        JSONArray dbTables = schemeJson.getJSONArray(FormConstants.DB_TABLE);
        Entity mainRecord = null;
        String mainTableName = StringPool.EMPTY;
        List<List<Entity>> subRecordList = new ArrayList<>();
        String mainId = IdWorker.get32UUID();
        JSONObject dataSettingJson = schemeJson.getJSONObject(FormConstants.DATA);
        JSONArray fields = dataSettingJson.getJSONArray(FormConstants.FIELDS);
        for (Object dbTableObj : dbTables) {
            JSONObject dbTable = (JSONObject) dbTableObj;
            boolean isMainTable = StringUtil.isBlank(dbTable.getString(FormConstants.DB_TABLE_RELATION_NAME));
            String tableName = dbTable.getString(FormConstants.DB_TABLE_NAME);
            if (isMainTable) {
                // 主表
                mainTableName = tableName;
                Map<String, Object> mainData = (Map<String, Object>) data.get(tableName);
                mainRecord = Entity.create(tableName);
                mainRecord.putAll(mainData);
                // 添加主键值
                mainRecord.put(dbTable.getString(FormConstants.DB_TABLE_PK), mainId);
            } else {
                // 子表
                List<Map<String, Object>> subDataList = (List<Map<String, Object>>) data.get(tableName);
                if (CollectionUtil.isEmpty(subDataList)) {
                    continue;
                }
                List<Entity> savedSubDataList = new ArrayList<>(subDataList.size());
                for (Map<String, Object> subData : subDataList) {
                    Entity record = Entity.create(tableName);
                    record.putAll(subData);
                    // 添加主键值
                    record.put(dbTable.getString(FormConstants.DB_TABLE_PK), IdWorker.get32UUID());
                    record.put(dbTable.getString(FormConstants.DB_TABLE_FIELD), mainId);
                    savedSubDataList.add(record);
                }
                subRecordList.add(savedSubDataList);
            }

        }
        for (Object fieldObj : fields) {
            JSONObject field = (JSONObject) fieldObj;
            JSONObject config = field.getJSONObject(FormConstants.CONFIG);
            String componentName = MapUtils.getString(config, "componentName");
            if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                JSONObject tabsMap = config.getJSONObject("childrenObj");
                for (Object tabObj : tabsMap.values()) {
                    JSONArray tabComponents = (JSONArray) tabObj;
                    for (Object tabComponentObj : tabComponents) {
                        JSONObject tabComponent = (JSONObject) tabComponentObj;
                        JSONObject tabFieldConfig = tabComponent.getJSONObject(FormConstants.CONFIG);
                        usingCodeRule(data, mainTableName, tabFieldConfig);
                    }
                }
            } else {
                usingCodeRule(data, mainTableName, config);
            }
        }
        try {
            // 保存主表数据
            Db.use(DataSourceUtil.getDataSource(dbLinkId)).insert(mainRecord);
            // 保存子表数据
            for (List<Entity> subRecords : subRecordList) {
                Db.use(DataSourceUtil.getDataSource(dbLinkId)).insert(subRecords);
            }
        } catch (SQLException e) {
            log.error("执行新增sql失败！", e);
            throw e;
        }
        return mainId;
    }

    private void usingCodeRule(Map<String, Object> data, String mainTableName, JSONObject config) {
        Map<String, Object> mainData = (Map<String, Object>) data.get(mainTableName);
        if (StringUtil.equalsIgnoreCase(config.getString(FormConstants.FORM_COMPONENT_TYPE), FormConstants.COMPONENT_TYPE_OF_CODE_RULE) &&
                !StringUtil.isEmpty(mainData.get(config.getString(FormConstants.BIND_TABLE_FIELD)))) {
            // 自动编码使用
            codeRuleService.useEncode(config.getString("autoCodeRule"));
        }
    }

    @Override
    public boolean updateCustomFormData(String formId, String recordId, Map<String, Object> data) {
        JSONObject schemeJson = getSchemeJsonByFormId(formId);
        String dbLinkId = schemeJson.getString(FormConstants.DB_LINK_ID);
        JSONArray dbTables = schemeJson.getJSONArray(FormConstants.DB_TABLE);
        List<SqlParam> sqlParamList = new ArrayList<>();
        for (Object dbTableObj : dbTables) {
            JSONObject dbTable = (JSONObject) dbTableObj;
            boolean isMainTable = StringUtil.isBlank(dbTable.getString(FormConstants.DB_TABLE_RELATION_NAME));
            String tableName = dbTable.getString(FormConstants.DB_TABLE_NAME);
            String pk = dbTable.getString(FormConstants.DB_TABLE_PK);
            if (isMainTable) {
                // 主表
                Map<String, Object> mainData = (Map<String, Object>) data.get(tableName);
                SqlParam sqlParam = FormSqlUtil.buildSimpleUpdateSqlParam(tableName, pk, recordId, mainData);
                sqlParamList.add(sqlParam);
            } else {
                // 子表
                // 先删除数据
                SqlParam deleteSqlParam = FormSqlUtil.buildSimpleDeleteSqlParam(tableName, dbTable.getString(FormConstants.DB_TABLE_FIELD), recordId);
                sqlParamList.add(deleteSqlParam);
                List<Map<String, Object>> subDataList = (List<Map<String, Object>>) data.get(tableName);
                for (Map<String, Object> subData : subDataList) {
                    // 添加主键值
                    subData.put(dbTable.getString(FormConstants.DB_TABLE_PK), IdWorker.get32UUID());
                    subData.put(dbTable.getString(FormConstants.DB_TABLE_FIELD), recordId);
                    SqlParam sqlParam = FormSqlUtil.buildSimpleInsertSqlParam(tableName, subData);
                    sqlParamList.add(sqlParam);
                }
            }

        }
        try {
            dbExecutor.batchExecute(dbLinkId, sqlParamList, false);

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteCustomFormData(String formId, String recordIds) {
        JSONObject schemeJson = getSchemeJsonByFormId(formId);
        String dbLinkId = schemeJson.getString(FormConstants.DB_LINK_ID);
        JSONArray dbTables = schemeJson.getJSONArray(FormConstants.DB_TABLE);

        List<SqlParam> sqlParamList = new ArrayList<>();
        for (Object dbTableObj : dbTables) {
            JSONObject dbTable = (JSONObject) dbTableObj;
            String tableName = dbTable.getString(FormConstants.DB_TABLE_NAME);
            boolean isMainTable = StringUtil.isBlank(dbTable.getString(FormConstants.DB_TABLE_RELATION_NAME));
            SqlParam sqlParam;
            String[] recordIdArray = StringUtils.split(recordIds, StringPool.COMMA);
            if (isMainTable) {
                sqlParam = FormSqlUtil.buildSimpleDeleteSqlParam(tableName, dbTable.getString(FormConstants.DB_TABLE_PK), recordIdArray);
            } else {
                sqlParam = FormSqlUtil.buildSimpleDeleteSqlParam(tableName, dbTable.getString(FormConstants.DB_TABLE_FIELD), recordIdArray);
            }
            sqlParamList.add(sqlParam);
        }
        try {
            dbExecutor.batchExecute(dbLinkId, sqlParamList, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public JSONObject getSchemeJsonByFormId(String formId) {
        XjrFormSchemeInfo schemeInfo = this.getById(formId);
        if (schemeInfo != null) {
            XjrFormScheme scheme = schemeService.getById(schemeInfo.getSchemeId());
            return JSONObject.parseObject(scheme.getScheme());
        }
        return null;
    }

    @Override
    public Object getCustomListData(String formId, JSONObject settingJson, Map<String, Object> params) {
        JSONObject schemeJson = getSchemeJsonByFormId(formId);
        JSONArray dbTables = schemeJson.getJSONArray(FormConstants.DB_TABLE);
        JSONObject columnData = settingJson.getJSONObject(FormConstants.COLUMN_DATA);
        JSONArray fields = columnData.getJSONArray(FormConstants.FIELDS);
        JSONObject form = columnData.getJSONObject(FormConstants.FORM);
        List<String> columnNameList = new ArrayList<>();
        String mainTableName = StringPool.EMPTY;
        String mainPk = StringPool.EMPTY;
        for (Object dbTableObj : dbTables) {
            JSONObject dbTable = (JSONObject) dbTableObj;
            if (StringUtil.isBlank(dbTable.getString(FormConstants.DB_TABLE_RELATION_NAME))) {
                mainTableName = dbTable.getString(FormConstants.DB_TABLE_NAME);
                mainPk = dbTable.getString(FormConstants.DB_TABLE_PK);
                columnNameList.add(mainPk);
                break;
            }
        }
        for (Object fieldObj : fields) {
            JSONObject field = (JSONObject) fieldObj;
            String columnName = field.getString(FormConstants.BIND_COLUMN);
            if (!StringUtil.isEmpty(columnName)) {
                columnNameList.add(columnName);
            }
        }

        JSONObject data = schemeJson.getJSONObject(FormConstants.DATA);
        JSONArray formFields = data.getJSONArray(FormConstants.FIELDS);

        Entity where = Entity.create(mainTableName).setFieldNames(columnNameList);
        JSONArray queryData = settingJson.getJSONArray(FormConstants.LIST_QUERY_DATA);
        for (Object formFieldObj : formFields) {
            JSONObject formField = (JSONObject) formFieldObj;
            JSONObject config = formField.getJSONObject(FormConstants.CONFIG);
            String componentType = config.getString(FormConstants.FORM_COMPONENT_TYPE);
            String componentName = MapUtils.getString(config, FormConstants.FORM_COMPONENT_NAME);
            if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                JSONObject tabsJson = config.getJSONObject("childrenObj");
                for (Object tabObj : tabsJson.values()) {
                    JSONArray tabComponents = (JSONArray) tabObj;
                    for (Object tabComponentObj : tabComponents) {
                        JSONObject tabComponent = (JSONObject) tabComponentObj;
                        JSONObject tabFieldConfig = tabComponent.getJSONObject(FormConstants.CONFIG);
                        buildQueryExpressions(params, where, queryData, tabFieldConfig, tabComponent.getString(FormConstants.FORM_COMPONENT_TYPE));
                    }
                }
            } else {
                buildQueryExpressions(params, where, queryData, config, componentType);
            }
        }
        String dbLinkId = schemeJson.getString(FormConstants.DB_LINK_ID);
        DataSource dataSource = DataSourceUtil.getDataSource(dbLinkId);
        List<Map<String, Object>> recordList = new ArrayList<>();
        int total = 0;
        try {
            int limit = NumberUtil.toInt(params.get(FormConstants.PARAM_KEY_PAGE_LIMIT));
            int size = NumberUtil.toInt(params.get(FormConstants.PARAM_KEY_PAGE_SIZE));
            PageUtil.setFirstPageNo(1);
            PageResult<Entity> page = Db.use(dataSource).page(where, limit, size);
            for (Entity entity : page) {
                CaseInsensitiveMap caseInsensitiveMap = new CaseInsensitiveMap(entity);
                Map<String, Object> map = new HashMap<>(entity.size());
                for (String columnName : columnNameList) {
                    Object value = caseInsensitiveMap.get(columnName);
                    if (StringUtil.equals(columnName, mainPk)) {
                        map.put(FormConstants.DEFAULT_ID_FIELD_NAME, value);
                    } else {
                        map.put(columnName, value);
                    }
                }
                recordList.add(map);
            }
            total = page.getTotal();
        } catch (SQLException e) {
            log.error("查询失败！", e);
        }
        // 数据源，数据字典等显示数据转换
        FormDataUtil.transFormListData(formFields, recordList);

        String isPage = form.getString(FormConstants.IS_PAGE);
        if (StringUtil.equals(isPage, "1")) {
            return ConventPage.getPageOutput(Long.valueOf(total), recordList);
        }
        return recordList;
    }

    private void buildQueryExpressions(Map<String, Object> params, Entity where, JSONArray queryData, JSONObject config, String componentType) {
        for (Object queryFieldObj : queryData) {
            JSONObject queryField = (JSONObject) queryFieldObj;
            String fieldName = queryField.getString(FormConstants.QUERY_FIELD_NAME);
            if (StringUtil.equals(config.getString(FormConstants.BIND_TABLE_FIELD), fieldName)) {
                Object value = params.get(fieldName);
                String operation = FormDataUtil.getQueryType(componentType);
                if (!StringUtil.isEmpty(value)) {
                    if (StringUtil.equalsIgnoreCase(operation, "equal")) {
                        where.set(fieldName, new Condition(fieldName, value));
                    } else if (StringUtil.equalsIgnoreCase(operation, "like")) {
                        where.set(fieldName, new Condition(fieldName, String.valueOf(value), Condition.LikeType.Contains));
                    }
                }
                if (StringUtil.equalsIgnoreCase(operation, "date")) {
                    Object start = params.get(fieldName + "_Start");
                    if (!StringUtil.isEmpty(start)) {
                        where.set(fieldName + "_Start", new Condition(fieldName, ">=", start));
                    }
                    Object end = params.get(fieldName + "_End");
                    if (!StringUtil.isEmpty(end)) {
                        where.set(fieldName + "_End", new Condition(fieldName, "<=", end));
                    }
                }
            }
        }
    }

    @Override
    public boolean createCustomFormTable(XjrFormScheme scheme, List<TableInfoDto> tableInfoDtoList) throws SQLException {
        String schemeStr = scheme.getScheme();
        JSONObject schemeJson = JSONObject.parseObject(schemeStr);
        String dbLinkId = schemeJson.getString(FormConstants.DB_LINK_ID);
        Connection connection = DataSourceUtil.getDataSource(dbLinkId).getConnection();
        JSONArray dbTables = schemeJson.getJSONArray(FormConstants.DB_TABLE);
        // 添加关联字段
        FormDataUtil.buildRelationField(dbTables, tableInfoDtoList);
        List<TableInfo> tableInfoList = tableInfoDtoList.stream().map(tableInfoDto -> {
            TableInfo tableInfo = BeanUtil.copy(tableInfoDto, TableInfo.class);
            tableInfo.setFields(BeanUtil.copyList(tableInfoDto.getFields(), TableField.class));
            // 添加公共字段
            TableInfoUtil.addComFieldAndKey(tableInfo);
            return tableInfo;
        }).collect(Collectors.toList());
        DbType dbType = JdbcUtils.getDbType(connection.getMetaData().getURL());
        List<String> sqls = SqlUtil.buildCreateTableSql(dbType, tableInfoList);
        try {
            dbExecutor.batchExecuteNonParam(dbLinkId, sqls.toArray(new String[]{}));
        } catch (SQLException e) {
            log.error("执行sql失败： " + sqls, e);
            throw e;
        }
        return true;
    }

    @Override
    public List<SystemFormVo> getSystemFormList(String keyword) {
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        return this.baseMapper.getSystemFormList(keyword);
    }

    public String getFullUrlOfSystemForm(String formId) {
        SystemFormVo systemForm = this.baseMapper.getSystemFormByFormId(formId);
        return systemForm.getUrl();
    }
}
