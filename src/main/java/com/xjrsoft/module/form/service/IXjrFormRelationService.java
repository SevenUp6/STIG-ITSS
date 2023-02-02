package com.xjrsoft.module.form.service;

import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.xjrsoft.module.form.dto.FormRelationPageListDto;
import com.xjrsoft.module.form.entity.XjrFormRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.form.vo.FormRelationVo;

/**
 * <p>
 * 表单-菜单关联关系表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface IXjrFormRelationService extends IService<XjrFormRelation> {

    PageOutput<FormRelationVo> getPageList(FormRelationPageListDto pageListDto);

    boolean addFormRelation(XjrFormRelation formRelation, XjrBaseModule module) throws Exception;

    boolean updateFormRelation(String formRelationId, XjrFormRelation formRelation, XjrBaseModule module) throws Exception;

    boolean deleteFormRelation(String...ids);

    XjrFormRelation getByModuleId(String id);
}
