package com.xjrsoft.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;

import java.util.List;

/**
 * 数据授权表 Mapper 接口
 *
 * @author job
 * @since 2021-01-27
 */
public interface XjrBaseDataAuthorizeMapper extends BaseMapper<XjrBaseDataAuthorize> {

    List<XjrBaseDataAuthorize> getDataAuthOfUserByUrl(String url, String userId, List<String> roleIdList);
}
