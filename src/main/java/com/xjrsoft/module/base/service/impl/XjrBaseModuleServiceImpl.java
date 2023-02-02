package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.mapper.XjrBaseModuleMapper;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.utils.ModuleUtils;
import com.xjrsoft.module.base.vo.ModuleOtherVo;
import com.xjrsoft.module.buildCode.dto.*;
import com.xjrsoft.module.form.constant.FormConstants;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-27
 */
@Service
@AllArgsConstructor
public class XjrBaseModuleServiceImpl extends ServiceImpl<XjrBaseModuleMapper, XjrBaseModule> implements IXjrBaseModuleService {

    private IXjrBaseRoleService roleService;

    private IXjrBaseModuleButtonService moduleButtonService;
    private IXjrBaseModuleColumnService moduleColumnService;
    private IXjrBaseModuleFormService moduleFormService;

    @Override
    public List<XjrBaseModule> getAuthModuleForCurrentUser(String systemId) {
        if (!SecureUtil.isAdminUser()) {
            return getAuthModuleForObject(SecureUtil.getUserId(), 2, systemId);
        } else {
            return getActiveModuleList(systemId);
        }
    }


    @Override
    public List<XjrBaseModule> getAuthModuleForObject(String objectId, Integer objectType, String systemId) {
        String userId = StringPool.EMPTY;
        List<String> roleIds = new ArrayList<>();
        if (objectType == 1) {
            roleIds.add(objectId);
        } else {
            userId = objectId;
            List<XjrBaseRole> roleList = roleService.getRolesByUserId(userId);
            if (CollectionUtil.isNotEmpty(roleList)) {
                roleList.stream().forEach(role -> roleIds.add(role.getRoleId()));
            }
        }
            return this.baseMapper.getModuleOfAuth(userId, roleIds, systemId);
    }

    @Override
    public List<ModuleOtherVo> getSubAuthorizations() {
        String userId = SecureUtil.getUserId();
        if (StringUtil.isEmpty(userId)) {
            return null;
        }
        boolean isAdminUser = SecureUtil.isAdminUser();
        // 查询用户的角色
        String[] roleIdArray = Func.toStrArray(SecureUtil.getUser().getRoleId());
        List<String> moduleIdList = baseMapper.getModuleIdsOfAuth(isAdminUser, 2, userId);
        if (!isAdminUser && roleIdArray != null && roleIdArray.length > 0) {
            moduleIdList.addAll(baseMapper.getModuleIdsOfAuth(false, 1, roleIdArray));
        }
        Map<String, ModuleOtherVo> moduleOtherVoMap = new HashMap<>(moduleIdList.size());
        if (CollectionUtil.isNotEmpty(moduleIdList)) {
            // 查询权限按钮
            // 用户
            List<XjrBaseModuleButton> userBtnList = baseMapper.getModuleBtnOfAuth(isAdminUser, moduleIdList, 2, userId);
            ModuleUtils.buildAuthBtnVo(userBtnList, moduleOtherVoMap);

            // 查询权限列表字段
            List<XjrBaseModuleColumn> userColList = baseMapper.getModuleColOfAuth(isAdminUser, moduleIdList, 2, userId);
            ModuleUtils.buildAuthColVo(userColList, moduleOtherVoMap);
            // 查询权限表单字段
            List<XjrBaseModuleForm> userFormList = baseMapper.getModuleFormOfAuth(isAdminUser, moduleIdList, 2, userId);
            ModuleUtils.buildAuthFormVo(userFormList, moduleOtherVoMap);
            if (!isAdminUser && roleIdArray != null && roleIdArray.length > 0) {
                // 角色
                List<XjrBaseModuleButton> roleBtnList = baseMapper.getModuleBtnOfAuth(false, moduleIdList, 1, roleIdArray);
                ModuleUtils.buildAuthBtnVo(roleBtnList, moduleOtherVoMap);

                List<XjrBaseModuleColumn> roleColList = baseMapper.getModuleColOfAuth(false, moduleIdList, 1, roleIdArray);
                ModuleUtils.buildAuthColVo(roleColList, moduleOtherVoMap);

                List<XjrBaseModuleForm> roleFormList = baseMapper.getModuleFormOfAuth(false, moduleIdList, 1, roleIdArray);
                ModuleUtils.buildAuthFormVo(roleFormList, moduleOtherVoMap);
            }
        }
        List<ModuleOtherVo> resultList = new ArrayList<>(moduleOtherVoMap.size());
        for (Map.Entry<String, ModuleOtherVo> entry : moduleOtherVoMap.entrySet()) {
            ModuleOtherVo value = entry.getValue();
            value.setModuleId(entry.getKey());
            resultList.add(value);
        }
        return resultList;
    }

    @Override
    public boolean saveModule(String moduleId, CodeSchemaDto codeSchemaDto) {
        ModuleDataDto moduleDataDto = codeSchemaDto.getModuleDataDto();
        BaseInfoDto baseInfoDto = codeSchemaDto.getBaseInfoDto();
        XjrBaseModule module = BeanUtil.copy(moduleDataDto, XjrBaseModule.class);
        module.setDeleteMark(0);
        module.setEnabledMark(1);
        module.setCreateDate(LocalDateTime.now());
        module.setTarget("iframe");
        module.setComponent(baseInfoDto.getFontDirectory() + "/" + baseInfoDto.getName() + "List");
        module.setModuleId(moduleId);
        module.setUrlAddress("/" + baseInfoDto.getName());
        if (StringUtil.isEmpty(module.getParentId())) {
            module.setParentId("0");
        }
        ColDataDto colDataDto = codeSchemaDto.getColDataDto();
        // 按钮
        List<Map<String, Object>> buttonList = colDataDto.getButtonList();
        List<XjrBaseModuleButton> buttonEntities = new ArrayList<>(buttonList.size());
        if (CollectionUtil.isNotEmpty(buttonList)) {
            for (Map<String, Object> button : buttonList) {
                XjrBaseModuleButton buttonEntity = new XjrBaseModuleButton();
                buttonEntity.setModuleId(moduleId);
                buttonEntity.setParentId(StringPool.ZERO);
                buttonEntity.setEnCode(MapUtils.getString(button, "val"));
                buttonEntity.setFullName(MapUtils.getString(button, "name"));
                buttonEntity.setIcon(MapUtils.getString(button, "icon"));
                buttonEntities.add(buttonEntity);
            }
        }
        moduleButtonService.saveBatch(buttonEntities);

        // 列表字段
        List<Map<String, Object>> columnList = colDataDto.getColumnList();
        List<XjrBaseModuleColumn> columnEntities = new ArrayList<>(columnList.size());
        if (CollectionUtil.isNotEmpty(columnList)) {
            for (Map<String, Object> column : columnList) {
                XjrBaseModuleColumn columnEntity = new XjrBaseModuleColumn();
                columnEntity.setFullName(MapUtils.getString(column, "fieldName"));
                columnEntity.setEnCode(MapUtils.getString(column, "bindColumn"));
                columnEntity.setModuleId(moduleId);
                columnEntity.setParentId(StringPool.ZERO);
                columnEntity.setSortCode(MapUtils.getInteger(column, "$index"));
                columnEntities.add(columnEntity);
            }
        }
        moduleColumnService.saveBatch(columnEntities);

        // 表单字段
        List<FormDataDto> formDataDtoList = codeSchemaDto.getFormDataDtoList();
        List<XjrBaseModuleForm> formList = new ArrayList<>(formDataDtoList.size());
        for (FormDataDto formDataDto : formDataDtoList) {
            Map<String, Object> config = formDataDto.getConfig();
            if (StringUtil.isEmpty(config.get(FormConstants.BIND_TABLE_FIELD))) {
                continue;
            }
            String componentName = MapUtils.getString(config, "componentName");
            if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
                Map<String, Object> tabsMap = MapUtils.getMap(config, "childrenObj");
                for (Object tabObj: tabsMap.values()) {
                    Collection<Map<String, Object>> tabComponents = (Collection<Map<String, Object>>) tabObj;
                    for (Map<String, Object> tabComponent : tabComponents) {
                        Map<String, Object> tabFieldConfig = MapUtils.getMap(tabComponent, FormConstants.CONFIG);
                        formList.add(buildFormField(moduleId, tabFieldConfig));
                    }
                }
            } else {
                formList.add(buildFormField(moduleId, config));
            }
        }
        moduleFormService.saveBatch(formList);

        return this.save(module);
    }

    private XjrBaseModuleForm buildFormField(String moduleId, Map<String, Object> config) {
        XjrBaseModuleForm form = new XjrBaseModuleForm();
        form.setModuleId(moduleId);
        String bindTableField = MapUtils.getString(config, "bindTableField");
        if (StringUtil.isNotBlank(bindTableField)) {
            form.setEnCode(bindTableField);
            form.setFullName(MapUtils.getString(config, "label"));
            Boolean required = MapUtils.getBoolean(config, "required");
            form.setIsRequired(required != null && required ? 1 : 0);
        } else {
            form.setEnCode(MapUtils.getString(config, "bindTable"));
        }
        return form;
    }

    @Override
    public List<XjrBaseModuleButton> getButtonsByModuleId(String moduleId) {
        return moduleButtonService.list(Wrappers.<XjrBaseModuleButton>query().lambda().eq(XjrBaseModuleButton::getModuleId, moduleId));
    }

    @Override
    public List<XjrBaseModuleColumn> getColumnsByModuleId(String moduleId) {
        return moduleColumnService.list(Wrappers.<XjrBaseModuleColumn>query().lambda().eq(XjrBaseModuleColumn::getModuleId, moduleId));
    }

    @Override
    public List<XjrBaseModuleForm> getFormsByModuleId(String moduleId) {
        return moduleFormService.list(Wrappers.<XjrBaseModuleForm>query().lambda().eq(XjrBaseModuleForm::getModuleId, moduleId));
    }

    @Override
    public boolean addModule(XjrBaseModule module, List<XjrBaseModuleButton> buttonList, List<XjrBaseModuleColumn> columnList, List<XjrBaseModuleForm> formList) throws Exception {
        //判断上级菜单是否为功能菜单
        XjrBaseModule xjrBaseModule = this.getOne(Wrappers.<XjrBaseModule>query().lambda().eq(XjrBaseModule::getModuleId, module.getParentId()));
        if (xjrBaseModule != null && "iframe".equals(xjrBaseModule.getTarget())) {
            throw new Exception("上级菜单不能为功能菜单!!!");
        }
        String moduleId = StringUtil.isNotBlank(module.getModuleId()) ? module.getModuleId() : IdWorker.get32UUID();
        module.setModuleId(moduleId);
        if (StringUtil.isEmpty(module.getParentId())) {
            module.setParentId(StringPool.ZERO);
        }
        if (CollectionUtil.isNotEmpty(buttonList)) {
            buttonList.forEach(button -> {
                button.setModuleId(moduleId);
                if (StringUtil.isEmpty(button.getParentId())) {
                    button.setParentId(StringPool.ZERO);
                }
            });
        }
        if (CollectionUtil.isNotEmpty(columnList)) {
            columnList.forEach(column -> {
                column.setModuleId(moduleId);
                if (StringUtil.isEmpty(column.getParentId())) {
                    column.setParentId(StringPool.ZERO);
                }
            });
        }
        if (CollectionUtil.isNotEmpty(formList)) {
            formList.forEach(form -> form.setModuleId(moduleId));
        }
        boolean isSuccess = this.save(module);
        if (isSuccess) {
            moduleButtonService.saveBatch(buttonList);
            moduleColumnService.saveBatch(columnList);
            moduleFormService.saveBatch(formList);
        }
        return isSuccess;
    }

    @Override
    public boolean updateModule(String moduleId, XjrBaseModule module, List<XjrBaseModuleButton> buttonList, List<XjrBaseModuleColumn> columnList, List<XjrBaseModuleForm> formList) throws Exception {
        //判断上级菜单是否为功能菜单
        XjrBaseModule xjrBaseModule = this.getOne(Wrappers.<XjrBaseModule>query().lambda().eq(XjrBaseModule::getModuleId, module.getParentId()));
        if (xjrBaseModule != null && "iframe".equals(xjrBaseModule.getTarget())) {
            throw new Exception("上级菜单不能为功能菜单!!!");
        }
        module.setModuleId(moduleId);
        if (CollectionUtil.isNotEmpty(buttonList)) {
            buttonList.forEach(button -> button.setModuleId(moduleId));
        }
        if (CollectionUtil.isNotEmpty(columnList)) {
            columnList.forEach(column -> column.setModuleId(moduleId));
        }
        if (CollectionUtil.isNotEmpty(formList)) {
            formList.forEach(form -> form.setModuleId(moduleId));
        }
        boolean isSuccess = this.updateById(module);
        if (isSuccess) {
            moduleButtonService.remove(Wrappers.<XjrBaseModuleButton>query().lambda().eq(XjrBaseModuleButton::getModuleId, moduleId));
            moduleButtonService.saveBatch(buttonList);

            moduleColumnService.remove(Wrappers.<XjrBaseModuleColumn>query().lambda().eq(XjrBaseModuleColumn::getModuleId, moduleId));
            moduleColumnService.saveBatch(columnList);

            moduleFormService.remove(Wrappers.<XjrBaseModuleForm>query().lambda().eq(XjrBaseModuleForm::getModuleId, moduleId));
            moduleFormService.saveBatch(formList);
        }
        return isSuccess;
    }

    @Override
    public List<XjrBaseModule> getModulesByParentId(String moduleId) {
        return this.list(Wrappers.<XjrBaseModule>query().lambda().eq(XjrBaseModule::getParentId, moduleId));
    }

    public List<XjrBaseModule> getActiveModuleList(String systemId) {
        return this.list(Wrappers.<XjrBaseModule>query().lambda().eq(XjrBaseModule::getDeleteMark, 0).eq(XjrBaseModule::getEnabledMark, 1)
                .eq(XjrBaseModule::getSubSystemId, systemId)
                .orderByAsc(XjrBaseModule::getSortCode));
    }
}
