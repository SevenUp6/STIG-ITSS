package com.xjrsoft.module.demo.service;

import com.xjrsoft.module.demo.entity.XjrBaseMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.demo.vo.MaterialVo;

import java.util.List;

/**
 * <p>
 * 物料表 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
public interface IXjrBaseMaterialService extends IService<XjrBaseMaterial> {

    List<MaterialVo> getMaterialsByItemIds(String orderId, List<String> itemIdList);
}
