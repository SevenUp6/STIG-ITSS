package com.xjrsoft.module.excel.parser;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.ExcelExportUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.excel.entity.XjrExcelImport;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import com.xjrsoft.module.excel.service.IXjrExcelImportService;
import com.xjrsoft.module.excel.service.IXjrExcelImportfiledsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Component
public class DefaultImportDataParser implements ImportDataParser {

    private IXjrBaseDataItemService dataItemService;

    private IXjrBaseDatasourceService datasourceService;

    private IXjrBaseUserService userService;

    private IXjrBaseDepartmentService departmentService;

    private IXjrBaseCompanyService companyService;

    private IXjrExcelImportfiledsService excelImportfiledsService;

    @Override
    public List<Map<String, Object>> parseDataToMap(XjrExcelImport excelImport, File excelFile) {
        List<XjrExcelImportfileds> fieldList = excelImportfiledsService.listByImportId(excelImport.getId());
        if (excelFile != null) {
            // multipartFile转file
//            File excelFile = IoUtil.toFile(file);
            // 获取上传文件的内容
            List<List<Object>> recordList = null;
            try {
                recordList = ExcelExportUtil.parseExcel(excelFile);
            } catch (IOException e) {
                log.error("解析excel文件数据失败！");
            }
            // 准备需要转换的数据
            Map<String, Map<String, String>> dataMap = ExcelExportUtil.buildTransData(fieldList);
            // 将数据写入对应表(根据导入按钮ID 查询数据库)
            List<Object> titleList = recordList.get(0);
            List<Map<String, Object>> resultList = new ArrayList<>(recordList.size());
            for (int i = 1; i < recordList.size(); i++) {
                Map<String, Object> data = new HashMap<>(titleList.size());
                for (XjrExcelImportfileds importField : fieldList) {
                    String colName = importField.getColName();
                    String fieldName = importField.getName();
                    Integer relationType = importField.getRelationType();
                    List<Object> record = recordList.get(i);
                    Object value = null;
                    if (relationType == 1) {
                        value = IdWorker.get32UUID();
                        data.put(fieldName, value);
                        continue;
                    }
                    for (int j = 0; j < titleList.size(); j++) {
                        String title = Func.toStr(titleList.get(j));
                        Object inputVale = record.get(j);
                        if ((StringUtil.isEmpty(colName) && StringUtil.equals(title, fieldName)) || StringUtil.equals(title, colName)) {
                            if (relationType == 2 || relationType == 3 || relationType == 10 || relationType == 11 || relationType == 12) {
                                Map<String, String> transDataMap = dataMap.get(fieldName);
                                value = transDataMap.get(inputVale);
                            } else if (relationType == 4) {
                                value = importField.getValue();
                            } else if (relationType == 5) {
                                value = SecureUtil.getUserId();
                            } else if (relationType == 6) {
                                value = SecureUtil.getUserName();
                            } else if (relationType == 7) {
                                value = new Date();
                            } else if (relationType == 8) {
//                                value = SecureUtil.getUser().get;
                            } else if (relationType == 9) {
//                                value = SecureUtil.getUserName();
                            } else {
                                value = inputVale;
                            }
                            break;
                        }
                    }
                    data.put(fieldName, value);
                }
                resultList.add(data);
            }
            return resultList;
        }
        return null;
    }
}
