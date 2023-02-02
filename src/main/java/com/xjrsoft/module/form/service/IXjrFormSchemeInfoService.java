package com.xjrsoft.module.form.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.form.dto.FormSchemeDto;
import com.xjrsoft.module.form.dto.FormSchemePageListDto;
import com.xjrsoft.module.form.dto.TableInfoDto;
import com.xjrsoft.module.form.entity.XjrFormScheme;
import com.xjrsoft.module.form.entity.XjrFormSchemeInfo;
import com.xjrsoft.module.form.vo.FormSchemeInfoVo;
import com.xjrsoft.module.form.vo.SystemFormVo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单信息表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface IXjrFormSchemeInfoService extends IService<XjrFormSchemeInfo> {

    boolean addFormScheme(XjrFormScheme scheme, XjrFormSchemeInfo schemeInfo);

    FormSchemeDto setDefauleValue(FormSchemeDto formSchemeDto);

    PageOutput<FormSchemeInfoVo> getPageList(FormSchemePageListDto pageListDto);

    boolean updateFormScheme(String schemeInfoId, XjrFormScheme scheme, XjrFormSchemeInfo schemeInfo);

    Map<String, Object> getCustomFormData(String formId, String recordId);

    String addCustomFormData(String formId, Map<String, Object> data) throws Exception;

    boolean updateCustomFormData(String formId, String recordId, Map<String, Object> data);

    boolean deleteCustomFormData(String formId, String keyValues);

    JSONObject getSchemeJsonByFormId(String formId);

    Object getCustomListData(String formId, JSONObject settingJson, Map<String, Object> params);

    boolean createCustomFormTable(XjrFormScheme scheme, List<TableInfoDto> tableInfoDtoList) throws SQLException;

    List<SystemFormVo> getSystemFormList(String keyword);

    String getFullUrlOfSystemForm(String formId);
}