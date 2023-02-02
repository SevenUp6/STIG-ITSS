package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.ModuleSubmitDto;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.utils.ModuleUtils;
import com.xjrsoft.module.base.vo.*;
import com.xjrsoft.module.buildCode.props.GlobalConfigProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/modules")
@Api(value = "/modules", tags = "系统菜单")
public class XjrBaseModuleController {
    private IXjrBaseRoleService roleService;
    private IXjrBaseAuthorizeService authorizeService;
    private IXjrBaseModuleService moduleService;
    private IXjrBaseModuleButtonService moduleButtonService;
    private IXjrBaseModuleColumnService moduleColumnService;
    private IXjrBaseModuleFormService moduleFormService;
    private IXjrBaseSubsystemService subsystemService;

    private IXjrBaseCommonModuleService comModuleService;

    public GlobalConfigProperties globalConfigProperties;

    @GetMapping("/vue")
    @ApiOperation(value = "获取首页树形菜单", notes = "获取首页树形菜单")
    public Response<List<MenuVo>> getTreeModules(@RequestParam(name = "systemId", required = false, defaultValue = "0") String systemId) {
        Boolean enabledSubSystem = this.globalConfigProperties.getEnabled_subSystem();
        if (!enabledSubSystem) {
            systemId = "0";
        } else {
            // 子系统权限
            List<XjrBaseSubsystem> curAuthList = subsystemService.getCurAuthList();
            if (CollectionUtil.isEmpty(curAuthList)) {
                return Response.ok(null, "没有任何系统的权限！");
            }
            List<String> systemIdList = curAuthList.stream().map(system -> system.getId()).collect(Collectors.toList());
            if (!SecureUtil.isAdminUser() && !systemIdList.contains(systemId)) {
                // 默认第一个系统
                systemId = systemIdList.get(0);
            }
        }

        List<XjrBaseModule> moduleList = moduleService.getAuthModuleForCurrentUser(systemId);
        List<MenuVo> menuVoList = BeanUtil.copyList(moduleList, MenuVo.class);
        return Response.ok(ForestNodeMerger.merge(menuVoList));
    }

    @GetMapping("/common")
    @ApiOperation(value = "获取用户的常用功能", notes = "获取用户的常用功能")
    public Response<List<CommonModuleVo>> getCommonModulesOfUser() {
        String userId = SecureUtil.getUserId();
        List<XjrBaseCommonModule> comModuleList = comModuleService.list(Wrappers.<XjrBaseCommonModule>query().lambda().eq(XjrBaseCommonModule::getUserId, userId).eq(XjrBaseCommonModule::getDeleteMark, 0).eq(XjrBaseCommonModule::getEnabledMark, 1).orderByAsc(XjrBaseCommonModule::getSortCode));
        List<CommonModuleVo> result = BeanUtil.copyList(comModuleList.stream().collect(Collectors.toList()), CommonModuleVo.class);
        return Response.ok(result, "查询成功！");
    }

    @GetMapping("/other")
    @ApiOperation(value = "获取菜单下组件", notes = "获取菜单下的按钮、列表字段、表单字段")
    public Response<List<ModuleOtherVo>> getSubAuthorizations(){
        List<ModuleOtherVo> moduleButtonList = moduleService.getSubAuthorizations();
        return Response.ok(moduleButtonList, "查询成功");
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取权限菜单树", notes = "获取（菜单，按钮，表单，字段）权限信息")
    public Response<Map<String, SystemModuleTreeVo>> getAllAuthModuleTree(){
        List<XjrBaseModule> moduleList = moduleService.list(Wrappers.<XjrBaseModule>query().lambda()
                .eq(!this.globalConfigProperties.getEnabled_subSystem(), XjrBaseModule::getSubSystemId, "0")
                .eq(XjrBaseModule::getEnabledMark, true).orderByAsc(XjrBaseModule::getSortCode));
        List<XjrBaseSubsystem> subsystemList = null;
        if (this.globalConfigProperties.getEnabled_subSystem()) {
            subsystemList = subsystemService.getActiveList();
        }
        List<ModuleVo> moduleVoList = BeanUtil.copyList(moduleList, ModuleVo.class);
        List<String> moduleIdList = new ArrayList<>();
        moduleVoList.forEach(moduleVo ->{
            if (StringUtil.equalsIgnoreCase(moduleVo.getTarget(), "iframe")) {
                moduleIdList.add(moduleVo.getModuleId());
            }
        });
        Map<String, SystemModuleTreeVo> resultMap = new HashMap<>(4);
        // 查询按钮
        List<XjrBaseModuleButton> moduleButtonList = moduleButtonService.list(Wrappers.<XjrBaseModuleButton>query().lambda().in(XjrBaseModuleButton::getModuleId, moduleIdList));
        List<ModuleVo> btnModuleVoList = ModuleUtils.buildSubModuleTree(moduleVoList, moduleButtonList, ModuleButtonVo.class);
        SystemModuleTreeVo systemBtnModuleTreeVo = ModuleUtils.buildSystemModuleVo(btnModuleVoList, subsystemList);
        resultMap.put("buttonList", systemBtnModuleTreeVo);

        // 查询列表字段
        List<XjrBaseModuleColumn> moduleColumnList = moduleColumnService.list(Wrappers.<XjrBaseModuleColumn>query().lambda().in(XjrBaseModuleColumn::getModuleId, moduleIdList));
        List<ModuleVo> colModuleVoList = ModuleUtils.buildSubModuleTree(moduleVoList, moduleColumnList, ModuleColumnVo.class);
        SystemModuleTreeVo systemColModuleTreeVo = ModuleUtils.buildSystemModuleVo(colModuleVoList, subsystemList);
        resultMap.put("columnList", systemColModuleTreeVo);

        // 查询表单字段
        List<XjrBaseModuleForm> moduleFormList = moduleFormService.list(Wrappers.<XjrBaseModuleForm>query().lambda().in(XjrBaseModuleForm::getModuleId, moduleIdList));
        List<ModuleVo> formModuleVoList = ModuleUtils.buildSubModuleTree(moduleVoList, moduleFormList, ModuleFormVo.class);
        SystemModuleTreeVo systemFormModuleTreeVo = ModuleUtils.buildSystemModuleVo(formModuleVoList, subsystemList);
        resultMap.put("formList", systemFormModuleTreeVo);

        List<ModuleVo> moduleVoTreeList = ForestNodeMerger.merge(moduleVoList);
        SystemModuleTreeVo systemModuleTreeVo = ModuleUtils.buildSystemModuleVo(moduleVoTreeList, subsystemList);
        resultMap.put("moduleList", systemModuleTreeVo);
        return Response.ok(resultMap);
    }

    @GetMapping
    @ApiOperation(value = "获取所有菜单")
    public Response getModules(){
        Boolean enabledSubSystem = this.globalConfigProperties.getEnabled_subSystem();
        List<XjrBaseModule> moduleList = moduleService.list((Wrappers.<XjrBaseModule>query().lambda()
                .eq(XjrBaseModule::getEnabledMark, 1)
                .eq(!enabledSubSystem, XjrBaseModule::getSubSystemId, "0")
                .orderByAsc(XjrBaseModule::getSortCode)));

        List<XjrBaseSubsystem> subsystemList = subsystemService.getActiveList();
        return Response.ok(ModuleUtils.buildSystemModule(moduleList, subsystemList));
    }

    @GetMapping("/{systemId}/system")
    @ApiOperation(value = "根据系统id获取旗下菜单")
    public Response<List<ModuleVo>> getModulesOfSystem(@PathVariable String systemId){
        if (!this.globalConfigProperties.getEnabled_subSystem()) systemId = "0";
        List<XjrBaseModule> moduleList = moduleService.list((Wrappers.<XjrBaseModule>query().lambda()
                .eq(XjrBaseModule::getEnabledMark, 1)
                .eq(XjrBaseModule::getSubSystemId, systemId)
                .orderByAsc(XjrBaseModule::getSortCode)));
        return Response.ok(ForestNodeMerger.merge(BeanUtil.copyList(moduleList, ModuleVo.class)));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取菜单详情")
    public Response<ModuleVo> getModuleById(@PathVariable String id){
        XjrBaseModule module = moduleService.getById(id);
        return Response.ok(BeanUtil.copy(module, ModuleVo.class));
    }

    @GetMapping("/buttons/{id}")
    @ApiOperation(value = "获取菜单下的按钮")
    public Response<List<ModuleButtonVo>> getButtonsByModuleId(@PathVariable String id){
        List<XjrBaseModuleButton> buttons = moduleService.getButtonsByModuleId(id);
        return Response.ok(BeanUtil.copyList(buttons, ModuleButtonVo.class));
    }

    @GetMapping("/columns/{id}")
    @ApiOperation(value = "获取菜单下的列表字段")
    public Response<List<ModuleColumnVo>> getColumnsByModuleId(@PathVariable String id){
        List<XjrBaseModuleColumn> columns = moduleService.getColumnsByModuleId(id);
        return Response.ok(BeanUtil.copyList(columns, ModuleColumnVo.class));
    }

    @GetMapping("/fomrs/{id}")
    @ApiOperation(value = "获取菜单下的表单字段")
    public Response<List<ModuleFormVo>> getModuleFormsByModuleId(@PathVariable String id){
        List<XjrBaseModuleForm> forms = moduleService.getFormsByModuleId(id);
        return Response.ok(BeanUtil.copyList(forms, ModuleFormVo.class));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除菜单")
    public Response deleteModule(@PathVariable String ids) {
        boolean isSuccess = false;
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        if (idArray.length == 1) {
            isSuccess = moduleService.removeById(ids);
        } else {
            isSuccess = moduleService.removeByIds(Arrays.asList(idArray));
        }
        return Response.status(isSuccess);
    }

    @PostMapping
    @ApiOperation(value = "新增菜单")
    public Response addModule(@RequestBody ModuleSubmitDto moduleSubmitDto) throws Exception {
        XjrBaseModule module = BeanUtil.copy(moduleSubmitDto.getModuleDto(), XjrBaseModule.class);
        List<XjrBaseModuleButton> buttonList = BeanUtil.copyList(moduleSubmitDto.getModuleButtonDtoList(), XjrBaseModuleButton.class);
        List<XjrBaseModuleColumn> columnList = BeanUtil.copyList(moduleSubmitDto.getModuleColumnDtoList(), XjrBaseModuleColumn.class);
        List<XjrBaseModuleForm> formList = BeanUtil.copyList(moduleSubmitDto.getModuleFormDtoList(), XjrBaseModuleForm.class);
        return Response.status(moduleService.addModule(module, buttonList, columnList, formList));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改菜单")
    public Response updateModule(@PathVariable String id, @RequestBody ModuleSubmitDto moduleSubmitDto) throws Exception {
        XjrBaseModule module = BeanUtil.copy(moduleSubmitDto.getModuleDto(), XjrBaseModule.class);
        List<XjrBaseModuleButton> buttonList = BeanUtil.copyList(moduleSubmitDto.getModuleButtonDtoList(), XjrBaseModuleButton.class);
        List<XjrBaseModuleColumn> columnList = BeanUtil.copyList(moduleSubmitDto.getModuleColumnDtoList(), XjrBaseModuleColumn.class);
        List<XjrBaseModuleForm> formList = BeanUtil.copyList(moduleSubmitDto.getModuleFormDtoList(), XjrBaseModuleForm.class);
        return Response.status(moduleService.updateModule(id, module, buttonList, columnList, formList));
    }

    @PutMapping("/{id}/child")
    @ApiOperation(value = "根据上级Id查询 菜单列表（包含自己）")
    public Response getModulesByParentId(@PathVariable String id) {
        List<XjrBaseModule> resultList = new ArrayList<>();
        XjrBaseModule parentModule = moduleService.getById(id);
        // 父菜单
        resultList.add(parentModule);
        // 子菜单
        resultList.addAll(moduleService.getModulesByParentId(id));
        return Response.ok(BeanUtil.copyList(resultList, ModuleVo.class));
    }

    @GetMapping("/current/authorized")
    @ApiOperation(value = "查询当前用户已授权的菜单、按钮、列表字段、表单字段树")
    public Response getAuthorizedModuleTreeOfCurUser() {
        String userId = SecureUtil.getUserId();
        Set<String> moduleIds = new LinkedHashSet<>();
        Set<String> moduleBtnIds = new LinkedHashSet<>();
        Set<String> moduleColIds = new LinkedHashSet<>();
        Set<String> moduleFormIds = new LinkedHashSet<>();
        List<XjrBaseRole> roleList = roleService.getRolesByUserId(userId);
        List<String> roleIds = CollectionUtil.isEmpty(roleList) ? null : roleList.stream().map(role -> role.getRoleId()).collect(Collectors.toList());
        Wrapper wrapper = Wrappers.<XjrBaseAuthorize>query().lambda().or(userWrapper -> userWrapper.eq(XjrBaseAuthorize::getObjectId, userId).eq(XjrBaseAuthorize::getObjectType, 2))
                .or(CollectionUtil.isNotEmpty(roleIds), roleWrapper -> roleWrapper.eq(XjrBaseAuthorize::getObjectType, 1).in(XjrBaseAuthorize::getObjectId, roleIds));
        List<XjrBaseAuthorize> authorizeList = authorizeService.list(wrapper);
        if (CollectionUtil.isNotEmpty(authorizeList)) {
            for (XjrBaseAuthorize authorize : authorizeList) {
                String itemId = authorize.getItemId();
                switch (authorize.getItemType()) {
                    case 1 :
                        moduleIds.add(itemId);
                        break;
                    case 2 :
                        moduleBtnIds.add(itemId);
                        break;
                    case 3 :
                        moduleColIds.add(itemId);
                        break;
                    case 4 :
                        moduleFormIds.add(itemId);
                        break;
                }
            }
        }
        Map<String, List<ModuleVo>> resultMap = new HashMap<>(4);
        if (CollectionUtil.isNotEmpty(moduleIds)) {
            List<XjrBaseModule> moduleList = moduleService.list(Wrappers.<XjrBaseModule>query().lambda().in(XjrBaseModule::getModuleId, moduleIds)
                    .eq(XjrBaseModule::getEnabledMark, 1).orderByAsc(XjrBaseModule::getSortCode));
            List<ModuleVo> moduleVoList = BeanUtil.copyList(moduleList, ModuleVo.class);
            // 按钮
            if (CollectionUtil.isNotEmpty(moduleBtnIds)) {
                List<XjrBaseModuleButton> moduleBtnList = moduleButtonService.list(Wrappers.<XjrBaseModuleButton>query().lambda()
                        .in(XjrBaseModuleButton::getModuleButtonId, moduleBtnIds).orderByAsc(XjrBaseModuleButton::getSortCode));
                resultMap.put("buttonList", ModuleUtils.buildSubModuleTree(moduleVoList, moduleBtnList, ModuleButtonVo.class));
            }
            //列表字段
            if (CollectionUtil.isNotEmpty(moduleColIds)) {
                List<XjrBaseModuleColumn> moduleColList = moduleColumnService.list(Wrappers.<XjrBaseModuleColumn>query().lambda()
                        .in(XjrBaseModuleColumn::getModuleColumnId, moduleBtnIds).orderByAsc(XjrBaseModuleColumn::getSortCode));
                resultMap.put("columnList", ModuleUtils.buildSubModuleTree(moduleVoList, moduleColList, ModuleButtonVo.class));
            }
            //表单字段
            if (CollectionUtil.isNotEmpty(moduleFormIds)) {
                List<XjrBaseModuleForm> moduleFormList = moduleFormService.list(Wrappers.<XjrBaseModuleForm>query().lambda()
                        .in(XjrBaseModuleForm::getModuleFormId, moduleBtnIds).orderByAsc(XjrBaseModuleForm::getSortCode));
                resultMap.put("formList", ModuleUtils.buildSubModuleTree(moduleVoList, moduleFormList, ModuleButtonVo.class));
            }
            //菜单
            List<ModuleVo> moduleVoTreeList = ForestNodeMerger.merge(moduleVoList);
            resultMap.put("moduleList", moduleVoTreeList);
        }
        return Response.ok(resultMap);
    }

    @GetMapping("/{url}/validate-url")
    @ApiOperation(value = "验证url/接口名重复")
    public Response validateUrl(@PathVariable String url) {
        int count = moduleService.count(Wrappers.<XjrBaseModule>query().lambda().eq(XjrBaseModule::getUrlAddress, StringPool.SLASH + url));
        return Response.ok(count > 0);
    }
}
