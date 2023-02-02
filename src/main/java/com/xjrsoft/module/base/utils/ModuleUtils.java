package com.xjrsoft.module.base.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.constants.ModuleConstants;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.vo.*;
import com.xjrsoft.module.form.constant.FormConstants;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleUtils {
    private ModuleUtils() {}

    public static void buildAuthBtnVo(List<XjrBaseModuleButton> btnList, Map<String, ModuleOtherVo> moduleOtherVoMap){
        if (CollectionUtil.isNotEmpty(btnList)) {
            for (XjrBaseModuleButton btn : btnList) {
                ModuleOtherVo moduleOtherVo = moduleOtherVoMap.get(btn.getModuleId());
                if (moduleOtherVo == null) {
                    moduleOtherVo = new ModuleOtherVo();
                    moduleOtherVoMap.put(btn.getModuleId(), moduleOtherVo);
                }
                moduleOtherVo.getButtons().add(btn.getEnCode());
            }
        }
    }

    public static void buildAuthColVo(List<XjrBaseModuleColumn> btnList, Map<String, ModuleOtherVo> moduleOtherVoMap){
        if (CollectionUtil.isNotEmpty(btnList)) {
            for (XjrBaseModuleColumn col : btnList) {
                ModuleOtherVo moduleOtherVo = moduleOtherVoMap.get(col.getModuleId());
                if (moduleOtherVo == null) {
                    moduleOtherVo = new ModuleOtherVo();
                    moduleOtherVoMap.put(col.getModuleId(), moduleOtherVo);
                }
                Map<String, String> colVo = new HashMap<>(2);
                colVo.put("label", col.getFullName());
                colVo.put("prop", col.getEnCode());
                moduleOtherVo.getColumns().add(colVo);
            }
        }
    }

    public static void buildAuthFormVo(List<XjrBaseModuleForm> formList, Map<String, ModuleOtherVo> moduleOtherVoMap){
        if (CollectionUtil.isNotEmpty(formList)) {
            for (XjrBaseModuleForm form : formList) {
                ModuleOtherVo moduleOtherVo = moduleOtherVoMap.get(form.getModuleId());
                if (moduleOtherVo == null) {
                    moduleOtherVo = new ModuleOtherVo();
                    moduleOtherVoMap.put(form.getModuleId(), moduleOtherVo);
                }
                moduleOtherVo.getForms().add(form.getEnCode());
            }
        }
    }

    public static <T extends IModuleVo> List<ModuleVo> buildSubModuleTree(List<ModuleVo> moduleVoList, List<?> moduleSubList, Class<T> subModuleClazz) {
        List<ModuleVo> subModuleVoList = BeanUtil.deepCopy(moduleVoList);
        Map<String, ModuleVo> subModuleMap = new HashMap<>();
        subModuleVoList.forEach(moduleVo ->{
            if (StringUtil.equalsIgnoreCase(moduleVo.getTarget(), "iframe")) {
                subModuleMap.put(moduleVo.getModuleId(), moduleVo);
            }
        });
        List<T> moduleSubVoList = BeanUtil.copyList(moduleSubList, subModuleClazz);
        List<T> moduleSubVoTreeList = ForestNodeMerger.merge(moduleSubVoList);
        List<String> subModuleIdList = new ArrayList<>();
        moduleSubVoTreeList.forEach(moduleSubVoTree -> {
            String moduleId = moduleSubVoTree.getModuleId();
            ModuleVo moduleVo = subModuleMap.get(moduleId);
            if (moduleVo != null) {
                moduleVo.getChildren().add(moduleSubVoTree);
                subModuleIdList.add(moduleId);
            }
        });
//         移除没有按钮、列表字段、表单字段的菜单
        List<ModuleVo> resultList = subModuleVoList.stream().filter(subModuleVo ->
                subModuleIdList.contains(subModuleVo.getModuleId()) || !StringUtil.equalsIgnoreCase(subModuleVo.getTarget(), "iframe")).collect(Collectors.toList());
        List<ModuleVo> subModuleVoTreeList = ForestNodeMerger.merge(resultList);
        return subModuleVoTreeList;
    }
    
    public static List<XjrBaseModuleButton> buildButtonEntities(String moduleId, JSONArray buttons) {
        List<XjrBaseModuleButton> resultList = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(buttons)) {
            int sortCode = 0;
            for (Object obj : buttons) {
                JSONObject button = (JSONObject) obj;
                XjrBaseModuleButton addBtn = new XjrBaseModuleButton();
                addBtn.setModuleId(moduleId);
                addBtn.setFullName(button.getString("name"));
                addBtn.setEnCode(button.getString("val"));
                addBtn.setParentId(StringPool.ZERO);
                addBtn.setSortCode(sortCode++);
                resultList.add(addBtn);
            }
//        }

        return resultList;
    }

    public static List<XjrBaseModuleColumn> buildColumnEntities(String moduleId, JSONArray columnsJson) {
        List<XjrBaseModuleColumn> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(columnsJson)) {
            for (Object columnObj : columnsJson) {
                XjrBaseModuleColumn column = new XjrBaseModuleColumn();
                JSONObject columnJson = (JSONObject) columnObj;
                column.setModuleColumnId(IdWorker.get32UUID());
                column.setModuleId(moduleId);
                column.setParentId(StringPool.ZERO);
                column.setFullName(columnJson.getString(ModuleConstants.COLUMN_NAME));
                column.setEnCode(columnJson.getString(ModuleConstants.COLUMN_FIELD));
                column.setSortCode(columnJson.getInteger(ModuleConstants.COLUMN_INDEX));
                resultList.add(column);
            }
        }
        return resultList;
    }

    public static List<XjrBaseModuleForm> buildFormEntities(String moduleId, JSONArray fieldsJson) {
        List<XjrBaseModuleForm> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fieldsJson)) {
            for (Object fieldObj : fieldsJson) {
                JSONObject fieldJson = (JSONObject) fieldObj;
                JSONObject configJson = fieldJson.getJSONObject(ModuleConstants.FORM_FIELD_CONFIG);
                String componentName = MapUtils.getString(configJson, "componentName");
                if (configJson.get(ModuleConstants.FORM_CHILDREN) != null
                        && !StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                    continue;
                }
                if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                    JSONObject tabsMap = configJson.getJSONObject("childrenObj");
                    for (Object tabObj: tabsMap.values()) {
                        JSONArray tabComponents = (JSONArray) tabObj;
                        for (Object tabComponentObj : tabComponents) {
                            JSONObject tabComponent = (JSONObject) tabComponentObj;
                            JSONObject tabFieldConfig = tabComponent.getJSONObject(FormConstants.CONFIG);
                            if (tabFieldConfig.get(ModuleConstants.FORM_CHILDREN) != null
                                    && !StringUtil.equalsIgnoreCase(MapUtils.getString(tabFieldConfig, "componentName"), "avue-tabs")) {
                                continue;
                            }
                            resultList.add(buildModuleForm(moduleId, tabFieldConfig));
                        }
                    }
                } else {
                    resultList.add(buildModuleForm(moduleId, configJson));
                }
            }
        }
        return resultList;
    }

    private static XjrBaseModuleForm buildModuleForm(String moduleId, JSONObject configJson) {
        XjrBaseModuleForm field = new XjrBaseModuleForm();
        field.setModuleFormId(IdWorker.get32UUID());
        field.setModuleId(moduleId);
        field.setFullName(configJson.getString(ModuleConstants.FORM_FIELD_LABEL));
        field.setEnCode(configJson.getString(ModuleConstants.FORM_FIELD));
        return field;
    }

    public static SystemModuleTreeVo buildSystemModule(List<XjrBaseModule> moduleList, List<XjrBaseSubsystem> subsystemList) {
        List<ModuleVo> moduleTreeList = ForestNodeMerger.merge(BeanUtil.copyList(moduleList, ModuleVo.class));
        return buildSystemModuleVo(moduleTreeList, subsystemList);
    }

    public static SystemModuleTreeVo buildSystemModuleVo(List<ModuleVo> moduleTreeList, List<XjrBaseSubsystem> subsystemList) {
        List<ModuleVo> mainModuleList = new ArrayList<>();
        List<SubSystemTreeVo> subSystemTreeVoList = null;
        for (ModuleVo moduleVo : moduleTreeList) {
            if (StringUtil.equalsIgnoreCase(moduleVo.getSubSystemId(), "0")) {
                mainModuleList.add(moduleVo);
                continue;
            }
            if (CollectionUtil.isNotEmpty(subsystemList)) {
                subSystemTreeVoList = BeanUtil.copyList(subsystemList, SubSystemTreeVo.class);
                for (SubSystemTreeVo subSystemTreeVo : subSystemTreeVoList) {
                    if (StringUtil.equalsIgnoreCase(subSystemTreeVo.getId(), moduleVo.getSubSystemId())) {
                        subSystemTreeVo.getModuleVoList().add(moduleVo);
                    }
                }
            }
        }
        SystemModuleTreeVo systemModuleTreeVo = new SystemModuleTreeVo();
        systemModuleTreeVo.setModuleVoList(mainModuleList);
        systemModuleTreeVo.setSubSystemTreeVoList(subSystemTreeVoList);
        return systemModuleTreeVo;
    }
}
