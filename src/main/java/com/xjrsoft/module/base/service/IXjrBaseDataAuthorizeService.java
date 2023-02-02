package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;

import java.util.List;
import java.util.Set;

/**
 * 数据授权表 服务类
 *
 * @author job
 * @since 2021-01-27
 */
public interface IXjrBaseDataAuthorizeService extends IService<XjrBaseDataAuthorize> {

    List<XjrBaseDataAuthorize> getAuthorizedDataListForObject(String objectId, Integer objectType);

    boolean authorizeData(XjrBaseDataAuthorize dataAuthorize, Set<String> moduleIds);

    List<XjrBaseDataAuthorize> getDataAuthListOfCurUserByUrl(String url);
}
