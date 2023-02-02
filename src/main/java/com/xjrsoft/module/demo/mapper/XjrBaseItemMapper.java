package com.xjrsoft.module.demo.mapper;

import com.xjrsoft.module.demo.entity.XjrBaseItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.demo.vo.ItemVo;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
public interface XjrBaseItemMapper extends BaseMapper<XjrBaseItem> {

    List<ItemVo> getListByOrderId(String orderId);
}
