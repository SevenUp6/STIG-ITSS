package com.xjrsoft.module.form.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.form.constant.FormConstants;
import com.xjrsoft.module.form.dto.TableFieldDto;
import com.xjrsoft.module.form.dto.TableInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FormDataUtil {

    private static final IXjrBaseDataItemService dataItemService;

    private static final IXjrBaseDatasourceService datasourceService;

    private static final IXjrBaseDepartmentService departmentService;

    private static final IXjrBaseCompanyService companyService;

    private static final IXjrBaseUserService userService;

    static {
        dataItemService = SpringUtil.getBean(IXjrBaseDataItemService.class);
        datasourceService = SpringUtil.getBean(IXjrBaseDatasourceService.class);
        departmentService = SpringUtil.getBean(IXjrBaseDepartmentService.class);
        companyService = SpringUtil.getBean(IXjrBaseCompanyService.class);
        userService = SpringUtil.getBean(IXjrBaseUserService.class);
    }

    private FormDataUtil(){}

    public static void transFormListData(JSONArray formFields, List<Map<String, Object>> records) {
        // 数据准备
        Map<String, Map<String, Object>> dataMap = new HashMap<>(16);
        List<String> transFieldList = new ArrayList<>();
        List<String> multiValueFieldList = new ArrayList<>();
        for (Object fieldObj : formFields) {
            Map<String, Object> data = new HashMap<>(16);
            JSONObject field = (JSONObject) fieldObj;
            JSONObject config = field.getJSONObject(FormConstants.CONFIG);
            String componentName = MapUtils.getString(config, FormConstants.FORM_COMPONENT_NAME);
            if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                JSONObject tabsJson = config.getJSONObject("childrenObj");
                for (Object tabObj: tabsJson.values()) {
                    JSONArray tabComponents = (JSONArray) tabObj;
                    for (Object tabComponentObj : tabComponents) {
                        JSONObject tabComponent = (JSONObject) tabComponentObj;
                        JSONObject tabFieldConfig = tabComponent.getJSONObject(FormConstants.CONFIG);
                        buildFieldData(dataMap, transFieldList, multiValueFieldList, data, tabComponent, tabFieldConfig);
                    }
                }
            } else {
                buildFieldData(dataMap, transFieldList, multiValueFieldList, data, field, config);
            }
        }

        for (Map<String, Object> record : records) {
            for (String fieldName : transFieldList) {
                String value = String.valueOf(record.get(fieldName));
                Map<String, Object> data = dataMap.get(fieldName);
                if (data == null) {
                    continue;
                }
                Object transToValue = null;
                if (multiValueFieldList.contains(fieldName) && !StringUtil.isEmpty(value)) {
                    String[] values = StringUtils.split(value, StringPool.COMMA);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < values.length; i++) {
                        if (i != 0) {
                            sb.append(StringPool.COMMA);
                        }
                        sb.append(data.get(values[i]));
                    }
                    transToValue = sb.toString();
                } else {
                    transToValue = data.get(value);
                }
                if (transToValue != null) {
                    record.put(fieldName, transToValue);
                }
            }
        }
    }

    private static void buildFieldData(Map<String, Map<String, Object>> dataMap, List<String> transFieldList, List<String> multiValueFieldList,
                                       Map<String, Object> data, JSONObject field, JSONObject config) {
        String componentType = config.getString(FormConstants.FORM_COMPONENT_TYPE);
        String fieldName = config.getString(FormConstants.BIND_TABLE_FIELD);
        String dataType = config.getString(FormConstants.FIELD_DATA_TYPE);
        String type = field.getString(FormConstants.FIELD_TYPE);
        if (!StringUtil.isEmpty(dataType)) {
            if (StringUtil.equalsIgnoreCase(dataType, FormConstants.FIELD_DATA_TYPE_OF_DATA_ITEM)) {
                // 数据字典
                String itemCode = config.getString(FormConstants.KEY_OF_FIELD_DATA_ITEM_CODE);
                List<XjrBaseDataItemDetail> dataItemDetails = dataItemService.getDataItemDetails(itemCode);
                for (XjrBaseDataItemDetail detail: dataItemDetails) {
                    data.put(detail.getItemValue(), detail.getItemName());
                }
            } else if (StringUtil.equalsIgnoreCase(dataType, FormConstants.FIELD_DATA_TYPE_OF_DATA_SOURCE)) {
                // 数据源
                String datasourceId = config.getString(FormConstants.KEY_OF_FIELD_DATA_SOURCE_ID);
                String showField = config.getString(FormConstants.KEY_OF_DATA_SOURCE_SHOW_FIELD);
                String saveField = config.getString(FormConstants.KEY_OF_DATA_SOURCE_SAVE_FIELD);
                String columns = showField + StringPool.COMMA + saveField;
                try {
                    List<Map<String, Object>> datasourceDataList = datasourceService.getDataByColumns(datasourceId, columns);
                    for (Map<String, Object> resultData : datasourceDataList) {
                        data.put(String.valueOf(resultData.get(saveField)), resultData.get(showField));
                    }
                } catch (Exception e) {
                    log.error("查询数据源失败！id: " + datasourceId, e);
                }
            }
            if (StringUtil.equalsIgnoreCase(componentType, FormConstants.COMPONENT_TYPE_OF_CHECKBOX)) {
                multiValueFieldList.add(fieldName);
            }
        } else if (!StringUtil.isEmpty(type) && (StringUtil.equalsIgnoreCase(FormConstants.FIELD_TYPE_OF_INFO, type) ||
                StringUtil.equalsIgnoreCase(FormConstants.FIELD_INFO_TYPE_OF_COMPANY, type) ||
                StringUtil.equalsIgnoreCase(FormConstants.FIELD_INFO_TYPE_OF_DEPARTMENT, type) ||
                StringUtil.equalsIgnoreCase(FormConstants.FIELD_INFO_TYPE_OF_USER, type))) {
            String infoType = Func.toStr(field.getString(FormConstants.FIELD_TYPE_OF_INFO_TYPE), type);
            if (StringUtil.equalsIgnoreCase(infoType, FormConstants.FIELD_INFO_TYPE_OF_USER)) {
                List<XjrBaseUser> allUserList = userService.getAllUserList();
                for (XjrBaseUser user : allUserList) {
                    data.put(user.getUserId(), user.getRealName());
                }
                multiValueFieldList.add(fieldName);
            } else if (StringUtil.equalsIgnoreCase(infoType, FormConstants.FIELD_INFO_TYPE_OF_DEPARTMENT)) {
                List<XjrBaseDepartment> allDepartmentList = departmentService.getAllDepartmentList();
                for (XjrBaseDepartment department : allDepartmentList) {
                    data.put(department.getDepartmentId(), department.getFullName());
                }
            } else if (StringUtil.equalsIgnoreCase(infoType, FormConstants.FIELD_INFO_TYPE_OF_COMPANY)) {
                List<XjrBaseCompany> companyList = companyService.getCompanyList();
                for (XjrBaseCompany company : companyList) {
                    data.put(company.getCompanyId(), company.getFullName());
                }
            } else {
                return;
            }
        } else {
            return;
        }
        dataMap.put(fieldName, data);
        transFieldList.add(fieldName);
    }

    /**
     *
     * @param componentType
     * @return
     */
    public static String getQueryType(String componentType){
        String resultType = com.baomidou.mybatisplus.core.toolkit.StringPool.EMPTY;
        if (StringUtil.equalsIgnoreCase(componentType, "input")
                || StringUtil.equalsIgnoreCase(componentType, "textarea")
                || StringUtil.equalsIgnoreCase(componentType, "ueditor")) {
            resultType = "like";
        } else if (StringUtil.equalsIgnoreCase(componentType, "datetime")) {
            resultType = "date";
        } else {
            resultType = "equal";
        }
        return resultType;
    }

    /**
     * @param tableRelationList 表关联关系对象集合
     * @param tableInfoList 表配置信息对象集合
     */
    public static void buildRelationField(JSONArray tableRelationList, List<TableInfoDto> tableInfoList) {
        if (CollectionUtil.isNotEmpty(tableRelationList) && tableRelationList.size() > 1) {
            String mainTableName = StringPool.EMPTY;
            Map<String, String> tableRelationFiledNameMap = new HashMap<>(tableInfoList.size() - 1);
            for (Object tableRelationObj : tableRelationList) {
                JSONObject tableRelation = (JSONObject) tableRelationObj;
                if (!StringUtil.isEmpty(tableRelation.getString(FormConstants.DB_TABLE_RELATION_NAME))) {
                    tableRelationFiledNameMap.put(tableRelation.getString(FormConstants.DB_TABLE_NAME), tableRelation.getString(FormConstants.DB_TABLE_FIELD));
                } else {
                    mainTableName = tableRelation.getString(FormConstants.DB_TABLE_NAME);
                }
            }
            for (TableInfoDto tableInfo : tableInfoList) {
                String relationFieldName = tableRelationFiledNameMap.get(tableInfo.getName());
                if(!StringUtil.isEmpty(relationFieldName)) {
                    TableFieldDto relationField = new TableFieldDto();
                    relationField.setLength(50);
                    relationField.setComment(mainTableName + "主键值");
                    relationField.setType("varchar");
                    relationField.setName(relationFieldName);
                    tableInfo.getFields().add(relationField);
                }
            }
        }
    }
}
