package com.xjrsoft.module.base.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.module.base.entity.XjrBaseCodeschema;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.vo.CodeSchemaListVo;

import java.util.List;

/**
 * <p>
 * 代码模板 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface XjrBaseCodeSchemaMapper extends BaseMapper<XjrBaseCodeschema> {

    List<CodeSchemaListVo> getPageList(String catalog, String keyword, IPage<CodeSchemaListVo> page);
}
