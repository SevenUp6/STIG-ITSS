package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageInput;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.entity.XjrBaseCoderule;
import com.xjrsoft.module.base.vo.CodeRuleVo;

/**
 * <p>
 * 编号规则表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface IXjrBaseCodeRuleService extends IService<XjrBaseCoderule> {
    XjrBaseCoderule getCodeRuleById(String id);

    PageOutput<CodeRuleVo> getCodeRulePageList(PageInput dto);

    String genEncode(String encode);

    boolean useEncode(String encode);
}
