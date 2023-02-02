package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.CodeSchemaListDto;
import com.xjrsoft.module.base.entity.XjrBaseCodeschema;
import com.xjrsoft.module.base.mapper.XjrBaseCodeSchemaMapper;
import com.xjrsoft.module.base.service.IXjrBaseCodeSchemaService;
import com.xjrsoft.module.base.vo.CodeSchemaListVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 代码模板 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Service
public class XjrBaseCodeSchemaServiceImpl extends ServiceImpl<XjrBaseCodeSchemaMapper, XjrBaseCodeschema> implements IXjrBaseCodeSchemaService {

    @Override
    public PageOutput<CodeSchemaListVo> getCodeSchemaPageList(CodeSchemaListDto dto) {
        String keyword = dto.getKeyword();
        if (!StringUtil.isEmpty(keyword)) keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        IPage<CodeSchemaListVo> page = ConventPage.getPage(dto);
        List<CodeSchemaListVo> pageList = this.baseMapper.getPageList(dto.getCatalog(), keyword, page);
        return ConventPage.getPageOutput(page.getTotal(), pageList);
    }
}
