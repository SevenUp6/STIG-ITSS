package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCodeschema;
import com.xjrsoft.module.base.entity.XjrBaseDatarelation;
import com.xjrsoft.module.base.mapper.XjrBaseDatarelationMapper;
import com.xjrsoft.module.base.service.IXjrBaseDatarelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.base.vo.CodeRuleVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 数据权限对应表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Service
public class XjrBaseDatarelationServiceImpl extends ServiceImpl<XjrBaseDatarelationMapper, XjrBaseDatarelation> implements IXjrBaseDatarelationService {
    @Override
    public PageOutput<XjrBaseDatarelation> getDataRelationPageList(GetPageListDto dto) {
        QueryWrapper<XjrBaseDatarelation> query = new QueryWrapper<>();
//        query.select(VoToColumn.Convert(CodeRuleVo.class));
        query.lambda().like(!StrUtil.hasBlank(dto.getKeyword()),XjrBaseDatarelation::getName,dto.getKeyword());
        IPage<XjrBaseDatarelation> xjrBaseDataRelationPage = baseMapper.selectPage(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(xjrBaseDataRelationPage);
    }

    @Override
    public boolean addDataRelation(XjrBaseDatarelation dataRelation){
        XjrUser user = SecureUtil.getUser();
        String dataRelationId = StringUtil.randomUUID();
        dataRelation.setId(dataRelationId);
        dataRelation.setCreateDate(LocalDateTime.now());
        dataRelation.setCreateUserId(user.getUserId());
        dataRelation.setCreateUserName(user.getUserName());
        return this.save(dataRelation);
    }

    @Override
    public boolean updateDataRelation(String dataRelationId, XjrBaseDatarelation dataRelation) {
        return this.updateById(dataRelation);
    }
}
