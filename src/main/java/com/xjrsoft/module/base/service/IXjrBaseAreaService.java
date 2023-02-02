package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.entity.XjrBaseArea;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行政区域表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface IXjrBaseAreaService extends IService<XjrBaseArea> {

    boolean addArea(XjrBaseArea area);

    boolean updateArea(String areaId, XjrBaseArea area);
}
