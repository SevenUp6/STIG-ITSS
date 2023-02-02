package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.dto.AuthorizeIdsDto;
import com.xjrsoft.module.base.entity.XjrBaseAuthorize;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 授权功能表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface IXjrBaseAuthorizeService extends IService<XjrBaseAuthorize> {

    /**
     *
     * @param objectId
     * @param objectType 1-角色， 2-人员
     * @param authorizeIdsVo
     * @return
     */
    boolean submit(String objectId, Integer objectType, AuthorizeIdsDto authorizeIdsDto);
}
