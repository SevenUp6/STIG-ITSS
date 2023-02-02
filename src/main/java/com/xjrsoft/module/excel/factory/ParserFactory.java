package com.xjrsoft.module.excel.factory;

import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.module.excel.parser.DefaultImportDataParser;
import com.xjrsoft.module.excel.parser.ImportDataParser;

public class ParserFactory {

    public static ImportDataParser getImportDataParser(){
        return SpringUtil.getBean(DefaultImportDataParser.class);
    }
}
