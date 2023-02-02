package com.xjrsoft.module.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.demo.entity.XjrBaseMaterial;
import com.xjrsoft.module.demo.vo.MaterialVo;

import java.util.List;

/**
 * <p>
 * 物料表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
public interface XjrBaseMaterialMapper extends BaseMapper<XjrBaseMaterial> {

    List<MaterialVo> getMaterialsByItemIds(String orderId, List<String> itemIdList);
}
