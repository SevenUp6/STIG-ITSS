package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.entity.XjrBaseDataItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;

import java.util.List;

/**
 * <p>
 * 数据字典分类表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
public interface IXjrBaseDataItemService extends IService<XjrBaseDataItem> {

    List<XjrBaseDataItemDetail> getDataItemDetails(String itemCode);

    boolean isUnique(String itemCode, String itemId);
}
