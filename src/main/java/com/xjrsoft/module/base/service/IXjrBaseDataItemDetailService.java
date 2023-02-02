package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据字典明细表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
public interface IXjrBaseDataItemDetailService extends IService<XjrBaseDataItemDetail> {

    List<XjrBaseDataItemDetail> getDataItemDetailListByCode(String itemCode, String keyword);

    List<XjrBaseDataItemDetail> queryItemDetailsBy(String fItemId, String nameOrValue);
}
