package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.vo.PageInfoVo;

public interface IXjrBaseWeChatService {
    Boolean updateInfo(String companyId) throws Exception;

    PageInfoVo getList(String keyword, Integer page, Integer pageSize,String companyId);
}
