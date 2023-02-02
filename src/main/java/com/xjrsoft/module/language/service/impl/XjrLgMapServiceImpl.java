package com.xjrsoft.module.language.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.language.dto.GetListLgMapDto;
import com.xjrsoft.module.language.entity.XjrLgMap;
import com.xjrsoft.module.language.mapper.XjrLgMapMapper;
import com.xjrsoft.module.language.service.IXjrLgMapService;
import com.xjrsoft.module.language.vo.LgMapVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 语言映照表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Service
public class XjrLgMapServiceImpl extends ServiceImpl<XjrLgMapMapper, XjrLgMap> implements IXjrLgMapService {

    @Override
    public List<XjrLgMap> getByCode(String code, String keyword) {
        QueryWrapper<XjrLgMap> query = new QueryWrapper<>();
        query.lambda().eq(XjrLgMap::getCode, code).like(StrUtil.isNotBlank(keyword), XjrLgMap::getName, keyword);
        return baseMapper.selectList(query);
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[dto]
     * @return:com.xjrsoft.common.page.PageOutput
     * @Description:获取分页数据
     */
    @Override
    public PageOutput<LgMapVo> getPageData(GetListLgMapDto dto) {
        Page page = new Page<>(dto.getLimit(), dto.getSize());
        List<Map<String, Object>> retList = baseMapper.getPageData(StrUtil.isNotBlank(dto.getKeyword()) ? StringPool.PERCENT + dto.getKeyword() + StringPool.PERCENT : null, null);
        page.setRecords(retList);
        return ConventPage.getPageOutput(page);
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[params]
     * @return:boolean
     * @Description:根据 F_ItemId / F_ModuleId 新增翻译
     */
    @Override
    public boolean saveLgByObjectId(Map<String, Object> params) {
        Map<String, String> map = (Map<String, String>) params.get("value");
        String code = StringUtil.randomUUID();
        map.forEach((key, value) -> {
            XjrLgMap xjrLgMap = new XjrLgMap();
            xjrLgMap.setId(StringUtil.randomUUID());
            xjrLgMap.setCode(code);
            xjrLgMap.setTypeCode(key);
            xjrLgMap.setName(value);
            baseMapper.insert(xjrLgMap);
        });
        return true;
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/12
    * @Param:[params]
    * @return:boolean
    * @Description:根据 F_ItemId / F_ModuleId 更新翻译
    */
    @Override
    public boolean updateLgByObjectId(Map<String, Object> map) {
        String fCode = String.valueOf(map.get("objectId"));
        Map<String, String> valueMap = (Map<String, String>) map.get("value");
        valueMap.forEach((key, value) -> {
            // 根据fcode查询
            QueryWrapper<XjrLgMap> query = new QueryWrapper<>();
            query.eq("F_Code", fCode).eq("F_TypeCode", key);
            XjrLgMap xjrLgMap = baseMapper.selectOne(query);
            xjrLgMap.setName(value);
            baseMapper.updateById(xjrLgMap);
        });
        return true;
    }



}
