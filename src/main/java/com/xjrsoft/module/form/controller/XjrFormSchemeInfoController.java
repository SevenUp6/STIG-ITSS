package com.xjrsoft.module.form.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.Enum.DeleteMarkEnum;
import com.xjrsoft.common.Enum.EnabledMarkEnum;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.dbmodel.DbExecutor;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.form.constant.FormConstants;
import com.xjrsoft.module.form.dto.FormSchemeDto;
import com.xjrsoft.module.form.dto.FormSchemePageListDto;
import com.xjrsoft.module.form.dto.SchemeDto;
import com.xjrsoft.module.form.dto.TableInfoDto;
import com.xjrsoft.module.form.entity.XjrFormRelation;
import com.xjrsoft.module.form.entity.XjrFormScheme;
import com.xjrsoft.module.form.entity.XjrFormSchemeInfo;
import com.xjrsoft.module.form.service.IXjrFormRelationService;
import com.xjrsoft.module.form.service.IXjrFormSchemeInfoService;
import com.xjrsoft.module.form.service.IXjrFormSchemeService;
import com.xjrsoft.module.form.vo.FormSchemeDetailVo;
import com.xjrsoft.module.form.vo.FormSchemeInfoVo;
import com.xjrsoft.module.form.vo.FormSchemeVo;
import com.xjrsoft.module.form.vo.SystemFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单信息表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/form-scheme")
@Api(value = "/form-scheme", tags = "自定义表单模块")
public class XjrFormSchemeInfoController {

    private IXjrFormSchemeInfoService schemeInfoService;

    private IXjrFormSchemeService schemeService;

    private IXjrFormRelationService formRelationService;

    private DbExecutor dbExecutor;

    @GetMapping
    @ApiOperation(value = "获取自定义表单详情列表 不分页")
    public Response<List<FormSchemeInfoVo>> getFormList(@RequestParam(name = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "F_Type", required = false) Integer type) {
        Wrapper<XjrFormSchemeInfo> wrapper = Wrappers.<XjrFormSchemeInfo>query().lambda()
                .select(XjrFormSchemeInfo.class, x -> VoToColumn.Convert(FormSchemeInfoVo.class).contains(x.getColumn()))
                .eq(XjrFormSchemeInfo::getType, 0)
                .like(StringUtil.isNotBlank(keyword), XjrFormSchemeInfo::getName, keyword)
                .orderByDesc(XjrFormSchemeInfo::getCreateDate);
        List<FormSchemeInfoVo> formListVoList = BeanUtil.copyList(schemeInfoService.list(wrapper), FormSchemeInfoVo.class);
        return Response.ok(formListVoList);
    }

    @GetMapping("/systemform")
    @ApiOperation(value = "获取自定义表单详情列表 不分页")
    public Response<List<SystemFormVo>> getSystemFormList(@RequestParam(name = "keyword", required = false) String keyword) {
        return Response.ok(schemeInfoService.getSystemFormList(keyword));
    }

    @PostMapping
    @ApiOperation(value="新增自定义表单")
    public Response addFormScheme(@RequestBody FormSchemeDto formSchemeDto) {
        XjrFormScheme scheme = BeanUtil.copy(formSchemeDto.getSchemeDto(), XjrFormScheme.class);
        XjrFormSchemeInfo schemeInfo = BeanUtil.copy(formSchemeDto.getSchemeInfoDto(), XjrFormSchemeInfo.class);
        // 设置表单类型
        schemeInfo.setType(0);
        return Response.status(schemeInfoService.addFormScheme(scheme, schemeInfo));
    }

    @PostMapping("/customtable")
    @ApiOperation(value="新增自定义表单(自定义数据库表)")
    public Response addFormSchemeWithCustomTable(@RequestBody FormSchemeDto formSchemeDto){
        SchemeDto schemeDto = formSchemeDto.getSchemeDto();
        if(schemeDto.getType()==2){
            //快速开发简易模板，设置默认值
             formSchemeDto = schemeInfoService.setDefauleValue(formSchemeDto);
        }
        XjrFormScheme scheme = BeanUtil.copy(formSchemeDto.getSchemeDto(), XjrFormScheme.class);
        XjrFormSchemeInfo schemeInfo = BeanUtil.copy(formSchemeDto.getSchemeInfoDto(), XjrFormSchemeInfo.class);
        List<TableInfoDto> tableInfoDtoList = formSchemeDto.getTableInfoDtoList();
        try {
            if (schemeInfoService.createCustomFormTable(scheme, tableInfoDtoList)) {
                return Response.status(schemeInfoService.addFormScheme(scheme, schemeInfo));
            }
        } catch (SQLException e) {
            return Response.notOk(e.getMessage());
        }
        return Response.status(false);
    }

    @PutMapping("/customtable/{id}")
    @ApiOperation(value = "修改自定义表单")
    public Response updateFormSchemeWithCustomTable(@PathVariable String id, @RequestBody FormSchemeDto formSchemeDto) {
        JSONObject oldScheme = schemeInfoService.getSchemeJsonByFormId(id);
        String dbLinkId = oldScheme.getString(FormConstants.DB_LINK_ID);
        // 删除旧表
        JSONArray dbTables = oldScheme.getJSONArray(FormConstants.DB_TABLE);
        for (Object dbTableObj : dbTables) {
            JSONObject dbTable = (JSONObject) dbTableObj;
            String tableName = dbTable.getString(FormConstants.DB_TABLE_NAME);
            String sql = "DROP TABLE " + tableName;
            try {
                dbExecutor.executeUpdate(dbLinkId, sql);
            } catch (SQLException e) {
                log.error("删除表失败! tableName: " + tableName, e);
//                return Response.notOk("删除表失败! tableName: " + tableName);
            }
        }
        SchemeDto schemeDto = formSchemeDto.getSchemeDto();
        if(schemeDto.getType()==2){
            //快速开发简易模板，设置默认值
            formSchemeDto = schemeInfoService.setDefauleValue(formSchemeDto);
        }
        XjrFormScheme scheme = BeanUtil.copy(formSchemeDto.getSchemeDto(), XjrFormScheme.class);
        XjrFormSchemeInfo schemeInfo = BeanUtil.copy(formSchemeDto.getSchemeInfoDto(), XjrFormSchemeInfo.class);
        List<TableInfoDto> tableInfoDtoList = formSchemeDto.getTableInfoDtoList();
        try {
            if (schemeInfoService.createCustomFormTable(scheme, tableInfoDtoList)) {
                return Response.status(schemeInfoService.updateFormScheme(id, scheme, schemeInfo));
            }
        } catch (SQLException e) {
            return Response.notOk(e.getMessage());
        }
        return Response.status(false);
    }

    @GetMapping("/page")
    @ApiOperation(value = "获取自定义表单列表 分页")
    public Response<PageOutput<FormSchemeInfoVo>> getPageList(FormSchemePageListDto pageListDto) {
        return Response.ok(schemeInfoService.getPageList(pageListDto));
    }

    @GetMapping("/{schemeId}")
    @ApiOperation(value = "获取自定义表单模板")
    public Response getSchemeById(@PathVariable String schemeId) {
        XjrFormScheme scheme = schemeService.getById(schemeId);
        if (scheme != null) {
            return Response.ok(BeanUtil.copy(scheme, FormSchemeVo.class));
        } else {
            return Response.ok();
        }

    }

    @GetMapping("/{id}/info")
    @ApiOperation(value = "获取自定义表单 与 详情")
    public Response<FormSchemeDetailVo> getSchemeInfoData(@PathVariable String id) {
        XjrFormSchemeInfo schemeInfo = schemeInfoService.getById(id);
        FormSchemeDetailVo schemeDetailVo = new FormSchemeDetailVo();
        if (schemeInfo != null) {
            schemeDetailVo.setFormSchemeInfoVo(BeanUtil.copy(schemeInfo, FormSchemeInfoVo.class));
            XjrFormScheme scheme = schemeService.getById(schemeInfo.getSchemeId());
            schemeDetailVo.setFormSchemeVo(BeanUtil.copy(scheme, FormSchemeVo.class));
        }
        return Response.ok(schemeDetailVo);
    }

    @GetMapping("/{id}/history")
    @ApiOperation(value = "获取自定义表单历史记录")
    public Response<List<FormSchemeVo>> history(@PathVariable String id) {
        Wrapper<XjrFormScheme> wrapper = Wrappers.<XjrFormScheme>query().lambda().select(XjrFormScheme.class, x -> VoToColumn.Convert(FormSchemeVo.class).contains(x.getColumn())).eq(XjrFormScheme::getSchemeInfoId, id).orderByDesc(XjrFormScheme::getCreateDate);
        List<FormSchemeVo> schemeVoList = BeanUtil.copyList(schemeService.list(wrapper), FormSchemeVo.class);
        return Response.ok(schemeVoList);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改自定义表单")
    public Response updateFormScheme(@PathVariable String id, @RequestBody FormSchemeDto formSchemeDto) {
        XjrFormScheme scheme = BeanUtil.copy(formSchemeDto.getSchemeDto(), XjrFormScheme.class);
        XjrFormSchemeInfo schemeInfo = BeanUtil.copy(formSchemeDto.getSchemeInfoDto(), XjrFormSchemeInfo.class);
        return Response.ok(schemeInfoService.updateFormScheme(id, scheme, schemeInfo));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除自定义表单")
    public Response deleteFormScheme(@PathVariable String ids) {
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idArray.length == 1) {
            isSuccess = schemeInfoService.removeById(ids);
        } else {
            isSuccess = schemeInfoService.removeByIds(Arrays.asList(idArray));
        }
        return Response.status(isSuccess);
    }

    @PatchMapping("/{id}/set-default/{schemeId}")
    @ApiOperation(value = "设为当前版本")
    public Response<Boolean> setDefault(@PathVariable String id, @PathVariable String schemeId) {
        XjrFormSchemeInfo schemeInfo = new XjrFormSchemeInfo();
        schemeInfo.setId(id);
        schemeInfo.setSchemeId(schemeId);
        return Response.status(schemeInfoService.updateById(schemeInfo));
    }

    @PatchMapping("/{id}/set-enable/{mark}")
    @ApiOperation(value = "修改启用状态 1 启用 0 不启用")
    public Response setEnableMark(@PathVariable String id, @PathVariable Integer mark) {
        XjrFormSchemeInfo schemeInfo = new XjrFormSchemeInfo();
        schemeInfo.setId(id);
        schemeInfo.setEnabledMark(mark);
        return Response.status(schemeInfoService.updateById(schemeInfo));
    }

    @GetMapping("/{id}/instance-form-list/{keyValue}")
    @ApiOperation(value = "查询自定义表单数据")
    public Response<Map<String, Object>> getCustomFormData(@PathVariable String id, @PathVariable String keyValue){
        Map<String, Object> resultData = schemeInfoService.getCustomFormData(id, keyValue);
        return Response.ok(resultData);
    }

    @PostMapping("/{id}/instance-form-list")
    @ApiOperation(value = "新增自定义表单数据")
    public Response<String> addCustomFormData(@PathVariable String id, @RequestBody Map<String, Object> data) throws Exception {
        String recordId = schemeInfoService.addCustomFormData(id, data);
        if (StringUtil.isNotBlank(recordId)) {
            return Response.ok(recordId);
        }
        return Response.notOk("");
    }

    @PutMapping("/{id}/instance-form-list/{keyValue}")
    @ApiOperation(value = "修改自定义表单数据")
    public Response updateCustomFormData(@PathVariable String id, @PathVariable String keyValue, @RequestBody Map<String, Object> data){
        boolean isSuccess = schemeInfoService.updateCustomFormData(id, keyValue, data);
        return Response.status(isSuccess);
    }

    @DeleteMapping("/{id}/instance-form-list/{keyValues}")
    @ApiOperation(value = "删除自定义表单数据")
    public Response deleteCustomFormData(@PathVariable String id, @PathVariable String keyValues){
        boolean isSuccess = schemeInfoService.deleteCustomFormData(id, keyValues);
        return Response.status(isSuccess);
    }

    @GetMapping("/{id}/instance-form-list")
    @ApiOperation(value = "查询自定义表单列表数据")
    public Response getListData(@PathVariable String id){
        Map<String, Object> params = WebUtil.getParametersStartingWith(WebUtil.getRequest(), null);
        XjrFormRelation formRelation = formRelationService.getByModuleId(id);
        String listSettingsStr = formRelation.getSettingJson();
        JSONObject settingJson = JSONObject.parseObject(listSettingsStr);
        return Response.ok(schemeInfoService.getCustomListData(formRelation.getFormId(), settingJson, params));
    }

    @GetMapping("getBySchemeId/{schemeId}")
    @ApiOperation(value = "查询表单数据根据schemeId")
    public Response getBySchemeId(@PathVariable String schemeId){
        List<XjrFormSchemeInfo> formSchemeInfos = schemeInfoService.list(Wrappers.<XjrFormSchemeInfo>query().lambda().eq(XjrFormSchemeInfo::getSchemeId, schemeId).eq(XjrFormSchemeInfo::getType, 2).eq(XjrFormSchemeInfo::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()).eq(XjrFormSchemeInfo::getEnabledMark, EnabledMarkEnum.ENABLED.getCode()));
        List<FormSchemeInfoVo> formSchemeInfoVos =new ArrayList<>();
        if(CollectionUtil.isNotEmpty(formSchemeInfos)) {
           formSchemeInfoVos = BeanUtil.copyList(formSchemeInfos, FormSchemeInfoVo.class);
       }
        return Response.ok(formSchemeInfoVos);
    }
}
