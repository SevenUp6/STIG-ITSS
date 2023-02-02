package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.entity.XjrBaseCodeRuleSeed;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 编号规则种子表 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-03-08
 */
public interface IXjrBaseCodeRuleSeedService extends IService<XjrBaseCodeRuleSeed> {

    XjrBaseCodeRuleSeed getCodeRuleSeedBy(String ruleId, String userId);
}
