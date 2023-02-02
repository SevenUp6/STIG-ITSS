//package com.xjrsoft.module.buildCode.service.impl;
//
//import com.baomidou.mybatisplus.core.toolkit.IdWorker;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.xjrsoft.core.tool.utils.BeanUtil;
//import com.xjrsoft.core.tool.utils.CollectionUtil;
//import com.xjrsoft.module.base.entity.XjrBaseModule;
//import com.xjrsoft.module.base.entity.XjrBaseModuleButton;
//import com.xjrsoft.module.base.entity.XjrBaseModuleColumn;
//import com.xjrsoft.module.base.entity.XjrBaseModuleForm;
//import com.xjrsoft.module.base.service.IXjrBaseModuleButtonService;
//import com.xjrsoft.module.base.service.IXjrBaseModuleColumnService;
//import com.xjrsoft.module.base.service.IXjrBaseModuleFormService;
//import com.xjrsoft.module.base.service.IXjrBaseModuleService;
//import com.xjrsoft.module.buildCode.dto.CodeSchemaDto;
//import com.xjrsoft.module.buildCode.dto.ColDataDto;
//import com.xjrsoft.module.buildCode.dto.FormDataDto;
//import com.xjrsoft.module.buildCode.dto.ModuleDataDto;
//import com.xjrsoft.module.buildCode.service.IBuildCodeService;
//import lombok.AllArgsConstructor;
//import org.apache.commons.collections.MapUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@AllArgsConstructor
//public class BuildCodeServiceImpl extends ServiceImpl implements IBuildCodeService {
//
//    private IXjrBaseModuleService moduleService;
//    private IXjrBaseModuleButtonService moduleButtonService;
//    private IXjrBaseModuleColumnService moduleColumnService;
//    private IXjrBaseModuleFormService moduleFormService;
//
//    public boolean saveModule(CodeSchemaDto codeSchemaDto) {
//        ModuleDataDto moduleDataDto = codeSchemaDto.getModuleDataDto();
//        XjrBaseModule module = BeanUtil.copy(moduleDataDto, XjrBaseModule.class);
//        String moduleId = IdWorker.get32UUID();
//        module.setModuleId(moduleId);
//        ColDataDto colDataDto = codeSchemaDto.getColDataDto();
//        // 按钮
//        List<Map<String, Object>> buttonList = colDataDto.getButtonList();
//        List<XjrBaseModuleButton> buttonEntities = new ArrayList<>(buttonList.size());
//        if (CollectionUtil.isNotEmpty(buttonList)) {
//            for (Map<String, Object> button : buttonList) {
//                XjrBaseModuleButton buttonEntity = new XjrBaseModuleButton();
//                buttonEntity.setModuleId(moduleId);
//                buttonEntity.setEnCode(MapUtils.getString(button, "val"));
//                buttonEntity.setFullName(MapUtils.getString(button, "name"));
//                buttonEntity.setIcon(MapUtils.getString(button, "icon"));
//                buttonEntities.add(buttonEntity);
//            }
//        }
//        moduleButtonService.saveBatch(buttonEntities);
//
//        // 列表字段
//        List<Map<String, Object>> columnList = colDataDto.getColumnList();
//        List<XjrBaseModuleColumn> columnEntities = new ArrayList<>(columnList.size());
//        if (CollectionUtil.isNotEmpty(columnList)) {
//            for (Map<String, Object> column : columnList) {
//                XjrBaseModuleColumn columnEntity = new XjrBaseModuleColumn();
//                columnEntity.setFullName(MapUtils.getString(column, "fieldName"));
//                columnEntity.setEnCode(MapUtils.getString(column, "bindColumn"));
//                columnEntity.setEnCode(moduleId);
//                columnEntity.setEnCode(MapUtils.getString(column, "$index"));
//                columnEntities.add(columnEntity);
//            }
//        }
//        moduleColumnService.saveBatch(columnEntities);
//
//        // 表单字段
//        List<FormDataDto> formDataDtoList = codeSchemaDto.getFormDataDtoList();
//        List<XjrBaseModuleForm> formList = new ArrayList<>(formDataDtoList.size());
//        for (FormDataDto formDataDto : formDataDtoList) {
//            XjrBaseModuleForm form = new XjrBaseModuleForm();
//            Map<String, Object> config = formDataDto.getConfig();
//            form.setModuleId(moduleId);
//            form.setEnCode(MapUtils.getString(config, "bindTableField"));
//            form.setEnCode(MapUtils.getString(config, "label"));
//            form.setIsRequired(MapUtils.getBoolean(config, "required") ? 1 : 0);
//            formList.add(form);
//        }
//        moduleFormService.saveBatch(formList);
//
//        return moduleService.save(module);
//    }
//}
