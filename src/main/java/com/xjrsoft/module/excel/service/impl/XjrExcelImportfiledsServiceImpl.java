package com.xjrsoft.module.excel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.mapper.XjrExcelImportfiledsMapper;
import com.xjrsoft.module.excel.service.IXjrExcelImportfiledsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Excel导入字段转换关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Service
public class XjrExcelImportfiledsServiceImpl extends ServiceImpl<XjrExcelImportfiledsMapper, XjrExcelImportfileds> implements IXjrExcelImportfiledsService {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[importId]
    * @return:java.util.List<com.xjrsoft.module.excel.entity.XjrExcelImportfileds>
    * @Description:根据importId获取导入字段
    */
    @Override
    public List<XjrExcelImportfileds> listByImportId(String importId) {
        return this.list(Wrappers.<XjrExcelImportfileds>query().lambda().eq(XjrExcelImportfileds::getImportId, importId).orderByAsc(XjrExcelImportfileds::getSortCode));
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[id]
    * @return:com.xjrsoft.module.excel.entity.XjrExcelImportfileds
    * @Description:获取主键字段
    */
    @Override
    public XjrExcelImportfileds getPmKey(String importId) {
        return this.getOne(Wrappers.<XjrExcelImportfileds>query().lambda().eq(XjrExcelImportfileds::getImportId, importId).eq(XjrExcelImportfileds::getRelationType, 1));
    }

    @Override
    public List<XjrExcelImportfileds> getByImportId(String importId) {
        return this.list(Wrappers.<XjrExcelImportfileds>query().lambda().eq(XjrExcelImportfileds::getImportId, importId).orderByAsc(XjrExcelImportfileds::getSortCode));
    }
}
