package com.xjrsoft.module.base.mapper;

import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典明细表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
public interface XjrBaseDataItemDetailMapper extends BaseMapper<XjrBaseDataItemDetail> {

    List<XjrBaseDataItemDetail> getDataItemDetailListByCode(String itemCode, String keyword);
}
