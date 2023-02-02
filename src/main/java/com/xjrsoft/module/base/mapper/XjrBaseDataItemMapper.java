package com.xjrsoft.module.base.mapper;

import com.xjrsoft.module.base.entity.XjrBaseDataItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;

import java.util.List;

/**
 * <p>
 * 数据字典分类表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
public interface XjrBaseDataItemMapper extends BaseMapper<XjrBaseDataItem> {

    List<XjrBaseDataItemDetail> getDataItemDetails(String itemCode);
}
