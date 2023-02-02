package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.mapper.XjrBaseDataItemDetailMapper;
import com.xjrsoft.module.base.service.IXjrBaseDataItemDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典明细表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@Service
public class XjrBaseDataItemDetailServiceImpl extends ServiceImpl<XjrBaseDataItemDetailMapper, XjrBaseDataItemDetail> implements IXjrBaseDataItemDetailService {

    @Override
    public List<XjrBaseDataItemDetail> getDataItemDetailListByCode(String itemCode, String keyword) {
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        return baseMapper.getDataItemDetailListByCode(itemCode, keyword);
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[fItemId:字典分类Id, nameOrValue:字典明细项目名或项目值]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseDataItemDetail>
    * @Description:根据字典类别id查询数据字典明细
    */
    @Override
    public List<XjrBaseDataItemDetail> queryItemDetailsBy(String fItemId, String nameOrValue) {
        QueryWrapper<XjrBaseDataItemDetail> query = new QueryWrapper<>();
        boolean isFuzzyQuery = StrUtil.isNotBlank(nameOrValue);
        query.eq(StrUtil.isNotBlank(fItemId), "F_ItemId", fItemId).eq("F_DeleteMark", 0);
        if (isFuzzyQuery) {
            String nameOrValueStr = StringPool.PERCENT + nameOrValue + StringPool.PERCENT;
            query.like("F_ItemName" , nameOrValueStr).or().like("F_ItemValue" , nameOrValueStr);
        }
        query.orderByAsc("F_SortCode");
        return baseMapper.selectList(query);
    }
}
