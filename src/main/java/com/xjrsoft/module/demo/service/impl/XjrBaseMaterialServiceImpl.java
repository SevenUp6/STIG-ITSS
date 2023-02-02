package com.xjrsoft.module.demo.service.impl;

import com.xjrsoft.module.demo.entity.XjrBaseMaterial;
import com.xjrsoft.module.demo.mapper.XjrBaseMaterialMapper;
import com.xjrsoft.module.demo.service.IXjrBaseMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.demo.vo.MaterialVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物料表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@Service
public class XjrBaseMaterialServiceImpl extends ServiceImpl<XjrBaseMaterialMapper, XjrBaseMaterial> implements IXjrBaseMaterialService {

    @Override
    public List<MaterialVo> getMaterialsByItemIds(String orderId, List<String> itemIdList) {
        return this.baseMapper.getMaterialsByItemIds(orderId, itemIdList);
    }
}
