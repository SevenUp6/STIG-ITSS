package com.xjrsoft.module.demo.service;

import com.xjrsoft.module.demo.entity.XjrBaseItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.demo.vo.ItemVo;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
public interface IXjrBaseItemService extends IService<XjrBaseItem> {

    List<ItemVo> getListByOrderId(String orderId);
}
