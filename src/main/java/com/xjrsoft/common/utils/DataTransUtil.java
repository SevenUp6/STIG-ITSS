package com.xjrsoft.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.Enum.TransDataType;
import com.xjrsoft.common.annotation.DataTrans;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class DataTransUtil {

    private static final IXjrBaseDataItemService dataItemService;

    private static final IXjrBaseDatasourceService datasourceService;

    private static final IXjrBaseDepartmentService departmentService;

    private static final IXjrBaseCompanyService companyService;

    private static final IXjrBaseUserService userService;

    private static final IXjrBaseAreaService areaService;

    static{
        dataItemService = SpringUtil.getBean(IXjrBaseDataItemService.class);
        datasourceService = SpringUtil.getBean(IXjrBaseDatasourceService.class);
        departmentService = SpringUtil.getBean(IXjrBaseDepartmentService.class);
        companyService = SpringUtil.getBean(IXjrBaseCompanyService.class);
        userService = SpringUtil.getBean(IXjrBaseUserService.class);
        areaService = SpringUtil.getBean(IXjrBaseAreaService.class);
    }

    private DataTransUtil(){}

    /**
     * 转换列表显示的值
     * @param transList
     * @param <T>
     */
    public static <T> void transListShowData(List<T> transList) {
        // 数据准备
        Map<String, Map<String, Object>> dataMap = new HashMap<>(16);
        List<String> multiValueFieldList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(transList)) {
            Map<String, DataTrans> fieldDataTransAnnotationMap = new HashMap<>(8);
            Map<String, Set<Object>> transValueMap = new HashMap<>(fieldDataTransAnnotationMap.size());
            Class<?> clazz = transList.get(0).getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                DataTrans annotation = field.getAnnotation(DataTrans.class);
                if (annotation == null) {
                    continue;
                }
                String name = field.getName();
                fieldDataTransAnnotationMap.put(name, annotation);
                for (T data : transList) {
                    Object property = BeanUtil.getProperty(data, name);
                    if (!StringUtil.isEmpty(property)) {
                        Set<Object> values = transValueMap.get(name);
                        if (CollectionUtil.isEmpty(values)) {
                            values = new LinkedHashSet<>();
                            transValueMap.put(name, values);
                        }
                        if (annotation.multi()) {
                            values.addAll(Arrays.asList(StringUtils.split(String.valueOf(property), StringPool.COMMA)));
                        } else {
                            values.add(property);
                        }
                    }
                }
            }
            // 查询转换的数据
            for (Map.Entry<String, DataTrans> entry : fieldDataTransAnnotationMap.entrySet()) {
                DataTrans annotation = entry.getValue();
                String fieldName = entry.getKey();
                Set<Object> values = transValueMap.get(fieldName);
                if (CollectionUtil.isEmpty(values)) {
                    continue;
                }
                Map<String, Object> data = new HashMap<>(16);
                TransDataType transDataType = annotation.dataType();
                switch (transDataType) {
                    case DATA_ITEM:
                        // 数据字典
                        String itemCode = annotation.dataCode();
                        List<XjrBaseDataItemDetail> dataItemDetails = dataItemService.getDataItemDetails(itemCode);
                        for (XjrBaseDataItemDetail detail: dataItemDetails) {
                            data.put(detail.getItemValue(), detail.getItemName());
                        }
                        break;
                    case DATA_SOURCE:
                        // 数据源
                        String datasourceId = annotation.dataCode();
                        String showField = annotation.showField();
                        String saveField = annotation.savedField();
                        String columns = showField + StringPool.COMMA + saveField;
                        try {
                            List<Map<String, Object>> datasourceDataList = datasourceService.getDataByColumns(datasourceId, columns);
                            for (Map<String, Object> resultData : datasourceDataList) {
                                data.put(String.valueOf(resultData.get(saveField)), resultData.get(showField));
                            }
                        } catch (Exception e) {
                            log.error("查询数据源失败！id: " + datasourceId, e);
                        }
                        break;
                    case COMPANY:
                        // 公司
                        List<XjrBaseCompany> companyList = OrganizationCacheUtil.getCacheListByIds(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, values);
                        for (XjrBaseCompany company : companyList) {
                            data.put(company.getCompanyId(), company.getFullName());
                        }
                        break;
                    case DEPARTMENT:
                        // 部门
                        List<XjrBaseDepartment> allDepartmentList = OrganizationCacheUtil.getCacheListByIds(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, values);
                        for (XjrBaseDepartment department : allDepartmentList) {
                            data.put(department.getDepartmentId(), department.getFullName());
                        }
                        break;
                    case USER:
                        // 人员
                        List<XjrBaseUser> allUserList = OrganizationCacheUtil.getCacheListByIds(OrganizationCacheUtil.USER_LIST_CACHE_KEY, values);
                        for (XjrBaseUser user : allUserList) {
                            data.put(user.getUserId(), user.getRealName());
                        }
                    case AREA:
                        List<XjrBaseArea> areaList = areaService.list(Wrappers.<XjrBaseArea>query().lambda().in(XjrBaseArea::getAreaCode, values));
                        for (XjrBaseArea area : areaList ) {
                            data.put(area.getAreaCode(), area.getAreaName());
                        }
                        break;
                }
                if (annotation.multi()) {
                    // 多选字段
                    multiValueFieldList.add(fieldName);
                }
                dataMap.put(fieldName, data);
            }

            // 填充转换数据
            for (T record : transList) {
                for (String fieldName : dataMap.keySet()) {
                    String property = Func.toStr(BeanUtil.getProperty(record, fieldName), StringPool.EMPTY);
                    Map<String, Object> data = dataMap.get(fieldName);
                    if (data == null) {
                        continue;
                    }
                    Object transToValue = null;
                    if (multiValueFieldList.contains(fieldName) && !StringUtil.isEmpty(property)) {
                        String[] values = StringUtils.split(property, StringPool.COMMA);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < values.length; i++) {
                            if (i != 0) {
                                sb.append(StringPool.COMMA);
                            }
                            sb.append(data.get(values[i]));
                        }
                        transToValue = sb.toString();
                    } else {
                        transToValue = data.get(property);
                    }
                    if (transToValue != null) {
                        BeanUtil.setProperty(record, fieldName, transToValue);
                    }
                }
            }
        }
    }
}
