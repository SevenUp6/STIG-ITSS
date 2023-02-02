package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDbDraft;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.mapper.XjrBaseDbdraftMapper;
import com.xjrsoft.module.base.service.IXjrBaseDbDraftService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.base.vo.CompanyPageListVo;
import com.xjrsoft.module.base.vo.DbDraftVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据表草稿 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Service
public class XjrBaseDbDraftServiceImpl extends ServiceImpl<XjrBaseDbdraftMapper, XjrBaseDbDraft> implements IXjrBaseDbDraftService {

    public PageOutput<DbDraftVo> getDbDraftPageList(GetPageListDto dto) {
        LambdaQueryWrapper<XjrBaseDbDraft> query = Wrappers.<XjrBaseDbDraft>query().lambda().like(!StrUtil.hasBlank(dto.getKeyword()), XjrBaseDbDraft::getName, dto.getKeyword());
        query.select(XjrBaseDbDraft.class, x -> VoToColumn.Convert(DbDraftVo.class).contains(x.getColumn()));
        IPage<XjrBaseDbDraft> dbFiledIPage = baseMapper.selectPage(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(dbFiledIPage, DbDraftVo.class);
    }
}
