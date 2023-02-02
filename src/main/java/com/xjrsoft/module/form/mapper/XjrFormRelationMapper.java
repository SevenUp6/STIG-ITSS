package com.xjrsoft.module.form.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.module.form.entity.XjrFormRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.form.vo.FormRelationVo;

import java.util.List;

/**
 * <p>
 * 表单-菜单关联关系表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface XjrFormRelationMapper extends BaseMapper<XjrFormRelation> {

    List<FormRelationVo> getPageList(String keyword, IPage page);
}
