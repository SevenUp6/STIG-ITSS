package com.xjrsoft.module.base.service;

import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCodeschema;
import com.xjrsoft.module.base.entity.XjrBaseDatarelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据权限对应表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
public interface IXjrBaseDatarelationService extends IService<XjrBaseDatarelation> {
    PageOutput<XjrBaseDatarelation> getDataRelationPageList(GetPageListDto dto);

    boolean addDataRelation(XjrBaseDatarelation codeSchema);

    boolean updateDataRelation(String dateRelationId, XjrBaseDatarelation dataRelation);
}
