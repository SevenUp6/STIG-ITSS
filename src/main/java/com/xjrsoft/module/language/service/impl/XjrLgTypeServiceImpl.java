package com.xjrsoft.module.language.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.language.dto.GetListLgTypeDto;
import com.xjrsoft.module.language.entity.XjrLgType;
import com.xjrsoft.module.language.mapper.XjrLgTypeMapper;
import com.xjrsoft.module.language.service.IXjrLgTypeService;
import com.xjrsoft.module.language.vo.LgTypeVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 多语言语言类型表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Service
public class XjrLgTypeServiceImpl extends ServiceImpl<XjrLgTypeMapper, XjrLgType> implements IXjrLgTypeService {

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[id]
    * @return:boolean
    * @Description:设置主语言
    */
    public boolean setMainlanguage(String id) {
        QueryWrapper<XjrLgType> query = new QueryWrapper<>();
        query.lambda().eq(XjrLgType::getIsMain, 1);
        XjrLgType oldXjrLgType = baseMapper.selectOne(query);
        if (oldXjrLgType != null) {
            oldXjrLgType.setIsMain(0);
            // 更新
            retBool(baseMapper.updateById(oldXjrLgType));
        }
        XjrLgType xjrLgType = baseMapper.selectById(id);
        if (xjrLgType != null) {
            xjrLgType.setIsMain(1);
            return retBool(baseMapper.updateById(xjrLgType));
        }
        return false;
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/12
    * @Param:[dto]
    * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.language.entity.XjrLgType>
    * @Description:获取分页数据
    */
    @Override
    public List<LgTypeVo> getPageData(GetListLgTypeDto dto) {
        QueryWrapper<XjrLgType> query = new QueryWrapper<>();
        query.select(XjrLgType.class, x -> VoToColumn.Convert(LgTypeVo.class).contains(x.getColumn()));
        query.lambda().like(StrUtil.isNotBlank(dto.getKeyword()), XjrLgType::getName, dto.getKeyword());
        List<XjrLgType> xjrLgTypeList = baseMapper.selectList(query);
        return BeanUtil.copyList(xjrLgTypeList, LgTypeVo.class);
    }
}
