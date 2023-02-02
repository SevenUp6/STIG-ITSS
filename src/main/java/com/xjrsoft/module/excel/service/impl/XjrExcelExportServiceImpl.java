package com.xjrsoft.module.excel.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.excel.dto.ExcelExportDto;
import com.xjrsoft.module.excel.dto.GetListExcelExportDto;
import com.xjrsoft.module.excel.entity.XjrExcelExport;
import com.xjrsoft.module.excel.mapper.XjrExcelExportMapper;
import com.xjrsoft.module.excel.service.IXjrExcelExportService;
import com.xjrsoft.module.excel.vo.ExcelExportVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Excel导入模板 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Service
public class XjrExcelExportServiceImpl extends ServiceImpl<XjrExcelExportMapper, XjrExcelExport> implements IXjrExcelExportService {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/10
    * @Param:[dto, fModuleId]
    * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.excel.entity.XjrExcelExport>
    * @Description:获取excel导出分页数据
    */
    @Override
    public PageOutput<ExcelExportVo> getPageData(GetListExcelExportDto dto) {
        QueryWrapper<XjrExcelExport> query = new QueryWrapper<>();
        query.select(XjrExcelExport.class, x -> VoToColumn.Convert(ExcelExportVo.class).contains(x.getColumn()))
             .lambda().like(!StrUtil.hasBlank(dto.getKeyword()),XjrExcelExport::getName,dto.getKeyword())
             .eq(StrUtil.isNotBlank(dto.getF_ModuleId()), XjrExcelExport::getModuleId, dto.getF_ModuleId());
        IPage<XjrExcelExport> dataPage = this.page(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(dataPage, ExcelExportVo.class);
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/14
    * @Param:[xjrExcelExport]
    * @return:boolean
    * @Description:保存excelExport
    */
    @Override
    public boolean saveExcelExport(ExcelExportDto dto) {
        XjrExcelExport xjrExcelExport = BeanUtil.copy(dto, XjrExcelExport.class);
        return this.save(xjrExcelExport);
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/14
    * @Param:[id, xjrExcelExport]
    * @return:boolean
    * @Description:更逊excelExport
    */
    @Override
    public boolean updateExcelExport(String id, ExcelExportDto dto) {
        XjrExcelExport xjrExcelExport = BeanUtil.copy(dto, XjrExcelExport.class);
        xjrExcelExport.setId(id);
        return this.updateById(xjrExcelExport);
    }
}
