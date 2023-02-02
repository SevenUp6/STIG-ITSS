package com.xjrsoft.module.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.demo.entity.XjrBaseItem;
import com.xjrsoft.module.demo.mapper.XjrBaseItemMapper;
import com.xjrsoft.module.demo.service.IXjrBaseItemService;
import com.xjrsoft.module.demo.vo.ItemVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@Service
public class XjrBaseItemServiceImpl extends ServiceImpl<XjrBaseItemMapper, XjrBaseItem> implements IXjrBaseItemService {

    public List<ItemVo> getListByOrderId(String orderId) {
        return this.baseMapper.getListByOrderId(orderId);
    }
}
