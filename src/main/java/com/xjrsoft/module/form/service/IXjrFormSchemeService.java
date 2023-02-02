package com.xjrsoft.module.form.service;

import com.xjrsoft.module.form.entity.XjrFormScheme;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自定义表单模板表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface IXjrFormSchemeService extends IService<XjrFormScheme> {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/14
    * @Param:[id]
    * @return:java.lang.String
    * @Description:
    */
    String getPkByFormInfoId(String id);
}
