package com.xjrsoft.module.language.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.language.dto.GetListLgMapDto;
import com.xjrsoft.module.language.entity.XjrLgMap;
import com.xjrsoft.module.language.vo.LgMapVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 语言映照表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface IXjrLgMapService extends IService<XjrLgMap> {

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[code]
    * @return:java.util.List<com.xjrsoft.module.language.entity.XjrLgMap>
    * @Description:根据F_Code查询
    */
    List<XjrLgMap> getByCode(String code, String keyword);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[dto]
    * @return:java.lang.Object
    * @Description:获取分页数据
    */
    PageOutput<LgMapVo> getPageData(GetListLgMapDto dto);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[params]
    * @return:java.lang.Object
    * @Description:根据 F_ItemId / F_ModuleId 新增翻译
    */
    boolean saveLgByObjectId(Map<String, Object> params);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[params]
    * @return:boolean
    * @Description:根据 F_ItemId / F_ModuleId 更新翻译
    */
    boolean updateLgByObjectId(Map<String, Object> params);
}
