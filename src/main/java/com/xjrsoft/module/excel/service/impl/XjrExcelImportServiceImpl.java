package com.xjrsoft.module.excel.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.ExcelExportUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.excel.dto.GetListExcelImportDto;
import com.xjrsoft.module.excel.entity.XjrExcelImport;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.mapper.XjrExcelImportMapper;
import com.xjrsoft.module.excel.service.IXjrExcelImportService;
import com.xjrsoft.module.excel.vo.ExcelImportVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Excel导出模板 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Service
public class XjrExcelImportServiceImpl extends ServiceImpl<XjrExcelImportMapper, XjrExcelImport> implements IXjrExcelImportService {

    @Autowired
    private XjrExcelImportfiledsServiceImpl excelImportFieldService;

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[dto, fModuleId]
     * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.excel.vo.ExcelImportVo>
     * @Description:查询列表页数据
     */
    @Override
    public PageOutput<ExcelImportVo> getPageData(GetListExcelImportDto dto) {
        QueryWrapper<XjrExcelImport> query = new QueryWrapper<>();
        query.select(XjrExcelImport.class, x -> VoToColumn.Convert(ExcelImportVo.class).contains(x.getColumn()))
                .lambda().like(StrUtil.isNotBlank(dto.getKeyword()), XjrExcelImport::getName, dto.getKeyword())
                .eq(StrUtil.isNotBlank(dto.getF_ModuleId()), XjrExcelImport::getModuleId, dto.getF_ModuleId());
        IPage<XjrExcelImport> dataPage = baseMapper.selectPage(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(dataPage, ExcelImportVo.class);
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[id]
     * @return:boolean
     * @Description:删除
     */
    @Transactional
    @Override
    public boolean delete(String id) {
        XjrExcelImport xjrExcelImport = this.baseMapper.selectById(id);
        if (xjrExcelImport != null) {
            String importId = xjrExcelImport.getId();
            if (baseMapper.deleteById(importId) > 0) {
                // 删除excelImportFields表数据
                return excelImportFieldService.remove(new QueryWrapper<XjrExcelImportfileds>().eq("F_ImportId", importId));
            }
        }
        return false;
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[moduleId, f_moduleBtnId]
     * @return:com.xjrsoft.module.excel.entity.XjrExcelImport
     * @Description:根据moduleId查询
     */
    @Override
    public XjrExcelImport getByModuleId(String moduleId, String moduleBtnId) {
        QueryWrapper query = new QueryWrapper();
        query.eq("F_ModuleId", moduleId);
        query.eq(StringUtils.isNotBlank(moduleBtnId), "F_ModuleBtnId", moduleBtnId);
        return baseMapper.selectOne(query);
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[file]
     * @return:java.io.File
     * @Description:multipartFile转File
     */
    @Override
    public File multipartFile2File(MultipartFile multfile) throws Exception {
        File excelFile = null;
        // 获取文件名
        String fileName = multfile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        excelFile = File.createTempFile(StringUtil.randomUUID(), prefix);
        // MultipartFile to File
        multfile.transferTo(excelFile);
        // 程序结束时，删除临时文件
        // boolean delete = excelFile.delete();
        return excelFile;
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[fDbTable, list, column, pmKey]
     * @return:java.lang.String
     * @Description:拼装插入sql
     */
    @Override
    public String assembleSql(String tableName, List<List<String>> valueList, String columnStr, String pmKey) {
        String sql = "";
        // 值List转换 List<List<String>> -> List<String>
        List<String> valList = new ArrayList<String>(10);
        // List<List<String>> -> List<String>
        for (int i = 1; i < valueList.size(); i++) {
            List<String> list2 = valueList.get(i);
            for (int j = 0; j < list2.size(); j++) {
                String string = "'" + list2.get(j) + "'";
                list2.set(j, string);
            }
            String fId = "'" + StringUtil.randomUUID() + "'";
            // 创建主键值
            list2.add(fId);
            String list2Str = list2.toString();
            String replace = list2Str.replace("[", "(").replace("]", ")");
            valList.add(replace);
        }
        String valStr = valList.toString();
        String valString = valStr.replace("[", "").replace("]", "");
        sql = "INSERT INTO " + tableName + "(" + columnStr + "," + pmKey + ") VALUE " + valString + ";";
        return sql;
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[titleData]
     * @return:java.io.File
     * @Description:下载模板方法
     */
    @Override
    public File downloadExcelModel(Map<String, String> titleData) throws IOException {
        File file = new File(ExcelExportUtil.getFilePath());
        return ExcelExportUtil.saveFile(titleData, new ArrayList<>(), file);
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/14
     * @Param:[dto]
     * @return:boolean
     * @Description:保存excelimport
     */
    @Transactional
    @Override
    public boolean saveExcelImport(XjrExcelImport excelImport, List<XjrExcelImportfileds> excelImportFieldList) {
        String excelImportId = IdWorker.get32UUID();
        excelImport.setId(excelImportId);
        if (this.save(excelImport)) {
            // 新增importField表数据
            for (XjrExcelImportfileds excelImportField : excelImportFieldList) {
                excelImportField.setImportId(excelImportId);
            }
            return excelImportFieldService.saveBatch(excelImportFieldList);
        }
        return false;
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/14
     * @Param:[id, dto]
     * @return:boolean
     * @Description:更新excelimport
     */
    @Override
    public boolean updateExcelImport(String id, XjrExcelImport excelImport, List<XjrExcelImportfileds> excelImportFieldList) {
        excelImport.setId(id);
        if (this.updateById(excelImport)) {
            // 删除xjrExcelImportFields
            Wrapper deleteWrapper = Wrappers.<XjrExcelImportfileds>query().lambda().eq(XjrExcelImportfileds::getImportId, id);
            excelImportFieldService.remove(deleteWrapper);
            // 重新添加
            for (XjrExcelImportfileds excelImportField : excelImportFieldList) {
                excelImportField.setImportId(id);
            }
            return excelImportFieldService.saveBatch(excelImportFieldList);
        }
        return false;
    }

    @Override
    public XjrExcelImport getByModuleId(String moduleId) {
        return this.getOne(Wrappers.<XjrExcelImport>query().lambda().eq(XjrExcelImport::getModuleId, moduleId).eq(XjrExcelImport::getEnabledMark, 1));
    }
}
