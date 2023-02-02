package com.xjrsoft.module.language.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.language.entity.XjrLgMap;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 语言映照表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface XjrLgMapMapper extends BaseMapper<XjrLgMap> {

    List<Map<String, Object>> getPageData(String fName, String itemNames);
}
