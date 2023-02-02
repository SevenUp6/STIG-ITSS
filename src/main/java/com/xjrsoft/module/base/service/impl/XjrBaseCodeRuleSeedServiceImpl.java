package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.base.entity.XjrBaseCodeRuleSeed;
import com.xjrsoft.module.base.mapper.XjrBaseCodeRuleSeedMapper;
import com.xjrsoft.module.base.service.IXjrBaseCodeRuleSeedService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 编号规则种子表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-03-08
 */
@Service
public class XjrBaseCodeRuleSeedServiceImpl extends ServiceImpl<XjrBaseCodeRuleSeedMapper, XjrBaseCodeRuleSeed> implements IXjrBaseCodeRuleSeedService {

    public XjrBaseCodeRuleSeed getCodeRuleSeedBy(String ruleId, String userId) {
        return this.getOne(Wrappers.<XjrBaseCodeRuleSeed>query().lambda()
                .eq(XjrBaseCodeRuleSeed::getRuleId, ruleId)
                .eq(XjrBaseCodeRuleSeed::getUserId, userId), false);
    }
}
