package com.xjrsoft.module.form.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.module.base.vo.ModuleTreeEntityVo;
import com.xjrsoft.module.form.entity.XjrFormSchemeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.form.vo.FormSchemeInfoVo;
import com.xjrsoft.module.form.vo.SystemFormVo;

import java.util.List;

/**
 * <p>
 * 自定义表单信息表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface XjrFormSchemeInfoMapper extends BaseMapper<XjrFormSchemeInfo> {

    List<FormSchemeInfoVo> getPageList(String category, String keyword, IPage page);

    List<SystemFormVo> getSystemFormList(String keyword);

    SystemFormVo getSystemFormByFormId(String formId);

    ModuleTreeEntityVo getModuleTreeEntityVoById(String moduleId);
}
