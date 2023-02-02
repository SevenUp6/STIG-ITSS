package com.xjrsoft.module.form.service.impl;

import com.xjrsoft.module.form.entity.XjrFormScheme;
import com.xjrsoft.module.form.mapper.XjrFormSchemeMapper;
import com.xjrsoft.module.form.service.IXjrFormSchemeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自定义表单模板表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Service
public class XjrFormSchemeServiceImpl extends ServiceImpl<XjrFormSchemeMapper, XjrFormScheme> implements IXjrFormSchemeService {

    @Override
    public String getPkByFormInfoId(String id) {
        return null;
    }
}
