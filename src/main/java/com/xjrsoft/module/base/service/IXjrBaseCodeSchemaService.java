package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.CodeSchemaListDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCodeschema;
import com.xjrsoft.module.base.vo.CodeSchemaListVo;
import com.xjrsoft.module.base.vo.CodeSchemaVo;

/**
 * <p>
 * 代码模板 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface IXjrBaseCodeSchemaService extends IService<XjrBaseCodeschema> {

    PageOutput<CodeSchemaListVo> getCodeSchemaPageList(CodeSchemaListDto dto);

}
