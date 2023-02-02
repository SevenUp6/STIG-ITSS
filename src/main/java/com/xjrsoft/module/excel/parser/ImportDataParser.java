package com.xjrsoft.module.excel.parser;

import com.xjrsoft.module.excel.entity.XjrExcelImport;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ImportDataParser {

    List<Map<String, Object>> parseDataToMap(XjrExcelImport excelImport, File excelFile);
}
