package com.xjrsoft.module.base.service.impl;

import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseArea;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.mapper.XjrBaseAreaMapper;
import com.xjrsoft.module.base.service.IXjrBaseAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行政区域表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Service
public class XjrBaseAreaServiceImpl extends ServiceImpl<XjrBaseAreaMapper, XjrBaseArea> implements IXjrBaseAreaService {
    @Override
    public boolean addArea(XjrBaseArea area){
        if (StringUtil.isEmpty(area.getParentId())) {
            area.setParentId("0");
        }
        return this.save(area);
    }

    @Override
    public boolean updateArea(String areaId, XjrBaseArea area) {
        area.setAreaId(areaId);
        return this.updateById(area);
    }
}