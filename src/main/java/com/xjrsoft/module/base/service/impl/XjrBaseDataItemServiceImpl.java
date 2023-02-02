package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDataItem;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.mapper.XjrBaseDataItemMapper;
import com.xjrsoft.module.base.service.IXjrBaseDataItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典分类表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@Service
public class XjrBaseDataItemServiceImpl extends ServiceImpl<XjrBaseDataItemMapper, XjrBaseDataItem> implements IXjrBaseDataItemService {

    @Override
    public List<XjrBaseDataItemDetail> getDataItemDetails(String itemCode) {
        return baseMapper.getDataItemDetails(itemCode);
    }

    public boolean isUnique(String itemCode, String itemId) {
        return this.count(Wrappers.<XjrBaseDataItem>query().lambda().eq(XjrBaseDataItem::getItemCode, itemCode).ne(!StringUtil.isEmpty(itemId), XjrBaseDataItem::getItemId, itemId)) < 1;
    }
}
