package com.xjrsoft.module.form.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.xjrsoft.module.base.entity.XjrBaseModuleButton;
import com.xjrsoft.module.base.entity.XjrBaseModuleColumn;
import com.xjrsoft.module.base.entity.XjrBaseModuleForm;
import com.xjrsoft.module.base.service.IXjrBaseModuleService;
import com.xjrsoft.module.base.utils.ModuleUtils;
import com.xjrsoft.module.excel.entity.XjrExcelImport;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.service.IXjrExcelImportService;
import com.xjrsoft.module.excel.service.IXjrExcelImportfiledsService;
import com.xjrsoft.module.form.constant.FormConstants;
import com.xjrsoft.module.form.dto.FormRelationPageListDto;
import com.xjrsoft.module.form.entity.XjrFormRelation;
import com.xjrsoft.module.form.mapper.XjrFormRelationMapper;
import com.xjrsoft.module.form.service.IXjrFormRelationService;
import com.xjrsoft.module.form.service.IXjrFormSchemeInfoService;
import com.xjrsoft.module.form.vo.FormRelationVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 表单-菜单关联关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Service
@AllArgsConstructor
public class XjrFormRelationServiceImpl extends ServiceImpl<XjrFormRelationMapper, XjrFormRelation> implements IXjrFormRelationService {

    private final IXjrBaseModuleService moduleService;

    private final IXjrFormSchemeInfoService schemeInfoService;

    private final IXjrExcelImportService importService;

    private final IXjrExcelImportfiledsService importFieldsService;

    @Override
    public PageOutput<FormRelationVo> getPageList(FormRelationPageListDto pageListDto) {
        String keyword = pageListDto.getKeyword();
        if (StringUtil.isNotBlank(keyword)) keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        IPage page = ConventPage.getPage(pageListDto);
        List<FormRelationVo> formRelationVoList = baseMapper.getPageList(keyword, page);
        return ConventPage.getPageOutput(page.getTotal(), formRelationVoList);
    }

    @Override
    public boolean addFormRelation(XjrFormRelation formRelation, XjrBaseModule module) throws Exception {
        String moduleId = StringUtil.randomUUID();
        formRelation.setModuleId(moduleId);
        module.setModuleId(moduleId);

        String settingJsonStr = formRelation.getSettingJson();
        JSONObject settingJson = JSONObject.parseObject(settingJsonStr);
        // 按钮
        JSONObject colData = settingJson.getJSONObject(FormConstants.COLUMN_DATA);
        JSONArray buttons = colData.getJSONArray(FormConstants.LIST_BUTTONS);
        List<XjrBaseModuleButton> buttonList = ModuleUtils.buildButtonEntities(moduleId, buttons);
        // 列表字段
        JSONArray colFields = colData.getJSONArray(FormConstants.FIELDS);
        List<XjrBaseModuleColumn> columnList = ModuleUtils.buildColumnEntities(moduleId, colFields);
        // 表单字段
        JSONObject scheme = schemeInfoService.getSchemeJsonByFormId(formRelation.getFormId());
        JSONObject data = scheme.getJSONObject(FormConstants.DATA);
        JSONArray formFields = data.getJSONArray(FormConstants.FIELDS);
        List<XjrBaseModuleForm> formList = ModuleUtils.buildFormEntities(moduleId, formFields);
        // 导入配置
        JSONObject importData = colData.getJSONObject(FormConstants.IMPORT_DATA);
        if (CollectionUtil.isNotEmpty(importData)) {
            saveOrUpdateImportData(moduleId, importData, scheme.getString(FormConstants.DB_LINK_ID), scheme.getJSONArray(FormConstants.DB_TABLE));
        }
        return this.save(formRelation) && moduleService.addModule(module, buttonList, columnList, formList);
    }

    private boolean saveOrUpdateImportData(String moduleId, JSONObject importData, String dbId, JSONArray dbTables) {
        boolean isNew = true;
        XjrExcelImport excelImport = new XjrExcelImport();
        XjrExcelImport oldExcelImport = importService.getOne(Wrappers.<XjrExcelImport>query().lambda().eq(XjrExcelImport::getModuleId, moduleId));
        if (oldExcelImport != null) {
            isNew = false;
            excelImport.setId(oldExcelImport.getId());
        } else {
            excelImport.setId(IdWorker.get32UUID());
        }
        excelImport.setModuleId(moduleId);
        excelImport.setName(importData.getString(FormConstants.IMPORT_MODULE_NAME));
        excelImport.setBtnName(importData.getString(FormConstants.IMPORT_BIND_BTN));
        excelImport.setDbId(dbId);
        // 设置导入表
        for (Object dbTableObj : dbTables) {
            JSONObject table = (JSONObject) dbTableObj;
            if (StringUtil.isEmpty(table.get(FormConstants.DB_TABLE_RELATION_NAME))) {
                excelImport.setDbTable(table.getString(FormConstants.DB_TABLE_NAME));
                break;
            }
        }
        JSONArray importFieldsJson = importData.getJSONArray(FormConstants.IMPORT_FIELDS);
        List<XjrExcelImportfileds> importFieldsList = new ArrayList<>();
        for (Object importFieldJsonObj : importFieldsJson) {
            JSONObject importFieldJson = (JSONObject) importFieldJsonObj;
            XjrExcelImportfileds importField = new XjrExcelImportfileds();
            importField.setImportId(excelImport.getId());
            importField.setName(importFieldJson.getString(FormConstants.IMPORT_FIELD_NAME));
            importField.setColName(importFieldJson.getString(FormConstants.IMPORT_FIELD_COL_NAME));
            importField.setRelationType(importFieldJson.getInteger(FormConstants.IMPORT_FIELD_RELATION_TYPE));
            importField.setOnlyOne(importFieldJson.getInteger(FormConstants.IMPORT_FIELD_ONLY_ONE));
            importField.setDataItemCode(importFieldJson.getString(FormConstants.IMPORT_FIELD_DATA_ITEM_CODE));
            importField.setDataSourceId(importFieldJson.getString(FormConstants.IMPORT_FIELD_DATASOURCE_ID));
            importField.setValue(importFieldJson.getString(FormConstants.IMPORT_FIELD_VALUE));
            importField.setSortCode(importFieldJson.getInteger(FormConstants.IMPORT_FIELD_SORT_CODE));
            importFieldsList.add(importField);
        }
        if (!isNew) {
            // 修改，删除原有的导入字段
            importFieldsService.remove(Wrappers.<XjrExcelImportfileds>query().lambda().eq(XjrExcelImportfileds::getImportId, excelImport.getId()));
        }
        return (isNew ? importService.save(excelImport) : importService.updateById(excelImport)) && importFieldsService.saveBatch(importFieldsList);
    }

    @Override
    public boolean updateFormRelation(String formRelationId, XjrFormRelation formRelation, XjrBaseModule module) throws Exception {
        String moduleId = formRelation.getModuleId();
        formRelation.setId(formRelationId);
        module.setModuleId(moduleId);
        String settingJsonStr = formRelation.getSettingJson();
        JSONObject settingJson = JSONObject.parseObject(settingJsonStr);
        // 按钮
        JSONObject colData = settingJson.getJSONObject(FormConstants.COLUMN_DATA);
        JSONArray buttons = colData.getJSONArray(FormConstants.LIST_BUTTONS);
        List<XjrBaseModuleButton> buttonList = ModuleUtils.buildButtonEntities(moduleId, buttons);
        // 列表字段
        JSONArray colFields = colData.getJSONArray(FormConstants.FIELDS);
        List<XjrBaseModuleColumn> columnList = ModuleUtils.buildColumnEntities(moduleId, colFields);
        // 表单字段
        JSONObject scheme = schemeInfoService.getSchemeJsonByFormId(formRelation.getFormId());
        schemeInfoService.update();
        JSONObject data = scheme.getJSONObject(FormConstants.DATA);
        JSONArray formFields = data.getJSONArray(FormConstants.FIELDS);
        List<XjrBaseModuleForm> formList = ModuleUtils.buildFormEntities(moduleId, formFields);
        // 导入配置
        JSONObject importData = colData.getJSONObject(FormConstants.IMPORT_DATA);
        if (CollectionUtil.isNotEmpty(importData)) {
            saveOrUpdateImportData(moduleId, importData, scheme.getString(FormConstants.DB_LINK_ID), scheme.getJSONArray(FormConstants.DB_TABLE));
        }
        boolean a = this.updateById(formRelation);
        boolean b = moduleService.updateModule(moduleId, module, buttonList, columnList, formList);
        return a&&b ;
    }

    @Override
    public boolean deleteFormRelation(String...ids) {
        if (ids != null && ids.length > 0) {
            List<XjrFormRelation> moduleIds = this.list(Wrappers.<XjrFormRelation>query().lambda().in(XjrFormRelation::getId, ids));
            moduleService.removeByIds(moduleIds.stream().map(formRelation -> formRelation.getModuleId()).collect(Collectors.toList()));
            return this.removeByIds(Arrays.asList(ids));
        }
        return false;
    }

    @Override
    public XjrFormRelation getByModuleId(String id) {
        return this.getOne(Wrappers.<XjrFormRelation>query().lambda().eq(XjrFormRelation::getModuleId, id));
    }
}
