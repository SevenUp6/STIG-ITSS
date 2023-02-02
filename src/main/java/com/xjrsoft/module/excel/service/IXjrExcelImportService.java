package com.xjrsoft.module.excel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.excel.dto.BaseExcelImportDto;
import com.xjrsoft.module.excel.dto.GetListExcelImportDto;
import com.xjrsoft.module.excel.entity.XjrExcelImport;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.vo.ExcelImportVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Excel导出模板 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
public interface IXjrExcelImportService extends IService<XjrExcelImport> {
    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[dto, fModuleId]
     * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.excel.vo.ExcelImportVo>
     * @Description:获取分页数据
     */
    PageOutput<ExcelImportVo> getPageData(GetListExcelImportDto dto);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[id]
    * @return:boolean
    * @Description:删除
    */
    boolean delete(String id);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[moduleId, f_moduleBtnId]
    * @return:com.xjrsoft.module.excel.entity.XjrExcelImport
    * @Description:根据moduleId查询
    */
    XjrExcelImport getByModuleId(String moduleId, String f_moduleBtnId);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[file]
    * @return:java.io.File
    * @Description:multipartFile转File
    */
    File multipartFile2File(MultipartFile file) throws Exception;

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[fDbTable, list, column, pmKey]
    * @return:java.lang.String
    * @Description:拼装插入sql
    */
    String assembleSql(String fDbTable, List<List<String>> list, String column, String pmKey);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/11
    * @Param:[titleDataList]
    * @return:java.lang.Object
    * @Description:下载excel模板
    */
    File downloadExcelModel(Map<String, String> titleDataList) throws IOException;

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/14
    * @Param:[dto]
    * @return:java.lang.Object
    * @Description:保存excelImport
    */
    boolean saveExcelImport(XjrExcelImport excelImport, List<XjrExcelImportfileds> excelImportFieldList);

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/14
     * @Param:[dto]
     * @return:java.lang.Object
     * @Description:更新excelImport
     */
    boolean updateExcelImport(String id, XjrExcelImport excelImport, List<XjrExcelImportfileds> excelImportFieldList);

    XjrExcelImport getByModuleId(String moduleId);
}
