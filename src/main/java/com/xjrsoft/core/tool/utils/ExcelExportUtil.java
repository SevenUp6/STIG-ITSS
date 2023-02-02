package com.xjrsoft.core.tool.utils;

import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

@Slf4j
public class ExcelExportUtil {

    private static final IXjrBaseDataItemService dataItemService;

    private static final IXjrBaseDatasourceService datasourceService;

    private static final IXjrBaseUserService userService;

    private static final IXjrBaseDepartmentService departmentService;

    private static final IXjrBaseCompanyService companyService;

    private static final String FILEPATH = IoUtil.getProjectPath() + File.separator + "temp";

    static {
        dataItemService = SpringUtil.getBean(IXjrBaseDataItemService.class);
        datasourceService = SpringUtil.getBean(IXjrBaseDatasourceService.class);
        userService = SpringUtil.getBean(IXjrBaseUserService.class);
        departmentService = SpringUtil.getBean(IXjrBaseDepartmentService.class);
        companyService = SpringUtil.getBean(IXjrBaseCompanyService.class);
    }

    private ExcelExportUtil(){}

    public static String getFilePath() {
        File dir = new File(FILEPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String title = FILEPATH + File.separator + System.currentTimeMillis() + "_统计报表.xlsx";
        return title;
    }

    public static File saveFile(Map<String, String> headData, List<Map> list, File file) throws IOException {
        // 创建工作薄
        Workbook workbook = null;
        if (StringUtils.endsWithIgnoreCase(file.getName(), ".xls")) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        // sheet:一张表的简称
        // row:表里的行
        // 创建工作薄中的工作表
        Sheet sheet = workbook.createSheet();
        // 创建行
        Row row = sheet.createRow(0);
        // 创建单元格，设置表头 创建列
        Cell cell = null;
        // 初始化索引
        int rowIndex = 0;
        int cellIndex = 0;

        // 创建标题行
        row = sheet.createRow(rowIndex);
        rowIndex++;
        // 遍历标题
        for (String h : headData.keySet()) {
            // 创建列
            cell = row.createCell(cellIndex);
            // 索引递增
            cellIndex++;
            // 逐列插入标题
            cell.setCellValue(headData.get(h));
        }

        // 得到所有记录 行：列
        // List<Record> list = Db.find(sql);
        Map<String, Object> map = null;

        if (list != null) {
            // 获取所有的记录 有多少条记录就创建多少行
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(rowIndex);
                // 得到所有的行 一个record就代表 一行
                map = list.get(i);
                // 下一行索引
                rowIndex++;
                // 刷新新行索引
                cellIndex = 0;
                // 在有所有的记录基础之上，便利传入进来的表头,再创建N行
                for (String h : headData.keySet()) {
                    cell = row.createCell(cellIndex);
                    cellIndex++;
                    // 按照每条记录匹配数据
                    cell.setCellValue(map.get(h) == null ? "" : map.get(h).toString());
                }
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
        return file;
    }

    public static String getCellStringValue(Cell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                DecimalFormat df = new DecimalFormat("0");
                cellValue = df.format(cell.getNumericCellValue());
                break;

            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;

            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue() + "";
                break;

            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula() + "";
                break;

            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;

            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "非法字符";
                break;

            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static Map<String, Map<String, String>> buildTransData(List<XjrExcelImportfileds> importFieldList) {
        Map<String, Map<String, String>> dataMap = new HashMap(16);
        for (XjrExcelImportfileds importField : importFieldList) {
            Integer relationType = importField.getRelationType();
            String fieldName = importField.getName();
            switch (relationType) {
                // 无关联
                case 0:
                    break;
                // 关联GUID
                case 1:
                    break;
                // 数据字典
                case 2:
                    String itemCode = importField.getDataItemCode();
                    List<XjrBaseDataItemDetail> dataItemDetailList = dataItemService.getDataItemDetails(itemCode);
                    Map<String, String> itemData = new HashMap<>(dataItemDetailList.size());
                    for (XjrBaseDataItemDetail itemDetail : dataItemDetailList) {
                        itemData.put(itemDetail.getItemName(), itemDetail.getItemValue());
                    }
                    dataMap.put(fieldName, itemData);
                    break;
                // 数据源
                case 3:
                    String[] props = StringUtils.split(importField.getDataSourceId(), StringPool.COMMA);
                    String columns = props[1] + StringPool.COMMA + props[2];
                    try {
                        List<Map<String, Object>> datasourceDataList = datasourceService.getDataByColumns(props[0], columns);
                        Map<String, String> datasourceData = new HashMap<>(datasourceDataList.size());
                        for (Map<String, Object> map : datasourceDataList) {
                            datasourceData.put(Func.toStr(map.get(map.get(props[1]))), Func.toStr(map.get(props[2])));
                        }
                        dataMap.put(fieldName, datasourceData);
                    } catch (Exception e) {
                        log.error("查询数据源失败！", e);
                    }
                    break;
                    // 固定数值
                case 4:
                    break;
                    // 操作人ID
                case 5:
                    break;
                    // 操作时间
                case 6:
                    break;
                    // 操作人名字
                case 7:
                    break;
                    //操作时间
                case 8:
                    break;
                    // 操作人公司
                case 9:
                    break;
                 // 公司
                case 10:
                    List<XjrBaseCompany> companyList = companyService.getCompanyList();
                    Map<String, String> companyData = new HashMap<>(companyList.size());
                    for (XjrBaseCompany company : companyList) {
                        companyData.put(company.getFullName(), company.getCompanyId());
                    }
                    dataMap.put(fieldName, companyData);
                    break;
                // 部门
                case 11:
                    List<XjrBaseDepartment> allDepartmentList = departmentService.getAllDepartmentList();
                    Map<String, String> departmentData = new HashMap<>(allDepartmentList.size());
                    for (XjrBaseDepartment department : allDepartmentList) {
                        departmentData.put(department.getFullName(), department.getDepartmentId());
                    }
                    dataMap.put(fieldName, departmentData);
                    break;
                // 人员ID
                case 12:
                    List<XjrBaseUser> allUserList = userService.getAllUserList();
                    Map<String, String> userData = new HashMap<>(allUserList.size());
                    for (XjrBaseUser user : allUserList) {
                        userData.put(user.getRealName(), user.getUserId());
                    }
                    dataMap.put(fieldName, userData);
                    break;
            }
        }
        return dataMap;
    }

    public static List<List<Object>> parseExcel(File file) throws IOException {
        List<List<Object>> result = new ArrayList<List<Object>>();
        String filePath = file.getPath();
        // 解析文件
        Workbook workbook = null;
        if (file.getName().toLowerCase().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
        } else {
            workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
        }
        Sheet sheet = workbook.getSheetAt(0);
        int maxRowNum = sheet.getLastRowNum() + 1;
        // 循环所有行
        for (int j = 0; j < maxRowNum; j++) {
            List<Object> rowList = new ArrayList<Object>();
            // 读取行
            Row row = sheet.getRow(j);
            if (row != null) {
                // getLastCellNum，是获取最后一个不为空的列是第几个
                for (int k = 0; k < row.getLastCellNum(); k++) {
                    Cell cell = row.getCell(k);
                    Object value = null;
                    if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = cell.getDateCellValue();
                    } else {
                        value = cell != null ? ExcelExportUtil.getCellStringValue(cell) : null;
                    }
                    rowList.add(value);
                }
            }
            result.add(rowList);
        }
        // 删除文件
        file.delete();
        return result;
    }
}
