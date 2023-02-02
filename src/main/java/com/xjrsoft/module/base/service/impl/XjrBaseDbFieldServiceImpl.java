package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.DbFieldDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDbField;
import com.xjrsoft.module.base.mapper.XjrBaseDbfieldMapper;
import com.xjrsoft.module.base.service.IXjrBaseDbFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.base.vo.DataSourceVo;
import com.xjrsoft.module.base.vo.DbFieldVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

/**
 * <p>
 * 建表常用字段 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Service
public class XjrBaseDbFieldServiceImpl extends ServiceImpl<XjrBaseDbfieldMapper, XjrBaseDbField> implements IXjrBaseDbFieldService {

    @Override
    public PageOutput<DbFieldVo> getDbDraftPageList(GetPageListDto pageListDto) {
        String keyword = pageListDto.getKeyword();
        LambdaQueryWrapper<XjrBaseDbField> queryWrapper = Wrappers.<XjrBaseDbField>query().lambda().like(StringUtil.isNotBlank(keyword), XjrBaseDbField::getName, keyword);
        queryWrapper.select(XjrBaseDbField.class, x -> VoToColumn.Convert(DbFieldVo.class).contains(x.getColumn()));
        IPage<XjrBaseDbField> page = this.page(ConventPage.getPage(pageListDto),queryWrapper);
        return ConventPage.getPageOutput(page, DbFieldVo.class);
    }
}
