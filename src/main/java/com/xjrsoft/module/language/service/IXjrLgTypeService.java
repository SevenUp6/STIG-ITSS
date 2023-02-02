package com.xjrsoft.module.language.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.language.dto.GetListLgTypeDto;
import com.xjrsoft.module.language.entity.XjrLgType;
import com.xjrsoft.module.language.vo.LgTypeVo;

import java.util.List;

/**
 * <p>
 * 多语言语言类型表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface IXjrLgTypeService extends IService<XjrLgType> {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/12
    * @Param:[id]
    * @return:boolean
    * @Description:设置主语言
    */
    boolean setMainlanguage(String id);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/12
    * @Param:[dto]
    * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.language.entity.XjrLgType>
    * @Description:获取分页数据
    */
    List<LgTypeVo> getPageData(GetListLgTypeDto dto);
}
