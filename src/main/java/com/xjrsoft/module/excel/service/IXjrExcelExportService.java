package com.xjrsoft.module.excel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.excel.dto.ExcelExportDto;
import com.xjrsoft.module.excel.dto.GetListExcelExportDto;
import com.xjrsoft.module.excel.entity.XjrExcelExport;
import com.xjrsoft.module.excel.vo.ExcelExportVo;

/**
 * <p>
 * Excel导入模板 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
public interface IXjrExcelExportService extends IService<XjrExcelExport> {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/10
    * @Param:[dto, fModuleId]
    * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.excel.entity.XjrExcelExport>
    * @Description:获取excel导出分页数据
    */
    PageOutput<ExcelExportVo> getPageData(GetListExcelExportDto dto);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/14
    * @Param:[xjrExcelExport]
    * @return:boolean
    * @Description:保存excelExport
    */
    boolean saveExcelExport(ExcelExportDto xjrExcelExport);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/14
    * @Param:[xjrExcelExport]
    * @return:boolean
    * @Description:保存excelExport
    */
    boolean updateExcelExport(String id, ExcelExportDto xjrExcelExport);
}
