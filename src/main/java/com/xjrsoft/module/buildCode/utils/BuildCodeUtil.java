package com.xjrsoft.module.buildCode.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.keywords.H2KeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.xjrsoft.common.dbmodel.utils.TableInfoUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.buildCode.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildCodeUtil {

    private static final String FILL_INSERT = "INSERT";
    private static final String FILL_UPDATE = "UPDATE";

    private BuildCodeUtil(){}

    public static List<TableInfo> buildTableInfoList(List<TableInfoDto> tableInfoDtoList, DbType dbType, StrategyConfig strategyConfig, GlobalConfig globalConfig) {
        List<TableInfo> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(tableInfoDtoList)) {
            for (TableInfoDto tableInfoDto : tableInfoDtoList) {
                TableInfo tableInfo = new TableInfo();
                String tableName = tableInfoDto.getName();
                tableInfo.setName(tableName);
                tableInfo.setComment(formatComment(tableInfoDto.getComment()));
                List<TableFieldDto> fields = tableInfoDto.getFields();
                List<TableField> tableFieldList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(fields)) {
                    for (TableFieldDto field : fields) {
                        TableField tableField = new TableField();
                        String columnName = field.getName();
                        tableField.setName(columnName);
                        String newColumnName = columnName;
                        IKeyWordsHandler keyWordsHandler = getKeyWordsHandler(dbType);
                        if (keyWordsHandler != null) {
                            if (keyWordsHandler.isKeyWords(columnName)) {
                                System.err.println(String.format("当前表[%s]存在字段[%s]为数据库关键字或保留字!", tableName, columnName));
                                tableField.setKeyWords(true);
                                newColumnName = keyWordsHandler.formatColumn(columnName);
                            }
                        }
                        tableField.setColumnName(newColumnName);
                        tableField.setType(field.getType());
                        INameConvert nameConvert = strategyConfig.getNameConvert();
                        if (null != nameConvert) {
                            tableField.setPropertyName(nameConvert.propertyNameConvert(tableField));
                        } else {
                            tableField.setPropertyName(strategyConfig, processName(tableField.getName(), NamingStrategy.underline_to_camel, null));
                        }
                        tableField.setColumnType(getTypeConvert(dbType).processTypeConvert(globalConfig, tableField));
                        tableField.setComment(formatComment(field.getComment()));
                        tableFieldList.add(tableField);
                    }
                }
                tableInfo.setFields(tableFieldList);
                // 添加公共字段
                addCommonFields(tableInfo, strategyConfig);
                tableInfo.setHavePrimaryKey(true);
                globalConfig.setIdType(IdType.ASSIGN_UUID);
                resultList.add(tableInfo);
            }
        }
        return resultList;
    }

    public static TableInfo addCommonFields(TableInfo tableInfo, StrategyConfig strategyConfig){
        List<TableField> fieldList = new ArrayList<>();

        // 主键 排第一位
        TableField id = new TableField();
        id.setName("F_Id");
        id.setColumnType(DbColumnType.STRING);
        id.setKeyFlag(true);
        id.setPropertyName(strategyConfig, "id");
        id.setColumnName("F_Id");
        id.setComment("主键值");
        fieldList.add(id);

        List<TableField> fields = tableInfo.getFields();
        if (CollectionUtil.isNotEmpty(fields)) {
            fieldList.addAll(fields);
        }

        TableField deleteMark = new TableField();
        strategyConfig.setLogicDeleteFieldName("F_DeleteMark");
        deleteMark.setName("F_DeleteMark");
        deleteMark.setColumnType(DbColumnType.INTEGER);
        deleteMark.setPropertyName(strategyConfig, "deleteMark");
        deleteMark.setColumnName("F_DeleteMark");
        deleteMark.setComment("删除标记");
        deleteMark.setFill(FILL_INSERT);
        fieldList.add(deleteMark);

        TableField enabledMark = new TableField();
        enabledMark.setName("F_EnabledMark");
        enabledMark.setColumnType(DbColumnType.INTEGER);
        enabledMark.setPropertyName(strategyConfig, "enabledMark");
        enabledMark.setColumnName("F_EnabledMark");
        enabledMark.setComment("有效标记");
        enabledMark.setFill(FILL_INSERT);
        fieldList.add(enabledMark);

        TableField createdDate = new TableField();
        createdDate.setName("F_CreateDate");
        createdDate.setColumnType(DbColumnType.LOCAL_DATE_TIME);
        createdDate.setPropertyName(strategyConfig, "createDate");
        createdDate.setColumnName("F_CreateDate");
        createdDate.setComment("创建时间");
        createdDate.setFill(FILL_INSERT);
        fieldList.add(createdDate);

        TableField createdUserId = new TableField();
        createdUserId.setName("F_CreateUserId");
        createdUserId.setColumnType(DbColumnType.STRING);
        createdUserId.setPropertyName(strategyConfig, "createUserId");
        createdUserId.setColumnName("F_CreateUserId");
        createdUserId.setComment("创建人主键值");
        createdUserId.setFill(FILL_INSERT);
        fieldList.add(createdUserId);

        TableField createdUserName = new TableField();
        createdUserName.setName("F_CreateUserName");
        createdUserName.setColumnType(DbColumnType.STRING);
        createdUserName.setPropertyName(strategyConfig, "createUserName");
        createdUserName.setColumnName("F_CreateUserName");
        createdUserName.setComment("创建人姓名");
        createdUserName.setFill(FILL_INSERT);
        fieldList.add(createdUserName);

        TableField modifyDate = new TableField();
        modifyDate.setName("F_ModifyDate");
        modifyDate.setColumnType(DbColumnType.LOCAL_DATE_TIME);
        modifyDate.setPropertyName(strategyConfig, "modifyDate");
        modifyDate.setColumnName("F_ModifyDate");
        modifyDate.setComment("修改时间");
        modifyDate.setFill(FILL_UPDATE);
        fieldList.add(modifyDate);

        TableField modifyUserId = new TableField();
        modifyUserId.setName("F_ModifyUserId");
        modifyUserId.setColumnType(DbColumnType.STRING);
        modifyUserId.setPropertyName(strategyConfig, "modifyUserId");
        modifyUserId.setColumnName("F_ModifyUserId");
        modifyUserId.setComment("修改人主键值");
        modifyUserId.setFill(FILL_UPDATE);
        fieldList.add(modifyUserId);

        TableField modifyUserName = new TableField();
        modifyUserName.setName("F_ModifyUserName");
        modifyUserName.setColumnType(DbColumnType.STRING);
        modifyUserName.setPropertyName(strategyConfig, "modifyUserName");
        modifyUserName.setColumnName("F_ModifyUserName");
        modifyUserName.setComment("修改人姓名");
        modifyUserName.setFill(FILL_UPDATE);
        fieldList.add(modifyUserName);

        tableInfo.setFields(fieldList);
        return tableInfo;
    }

    private static String processName(String name, NamingStrategy strategy, String[] prefix) {
        String propertyName;
        if (ArrayUtils.isNotEmpty(prefix)) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }

    public static ITypeConvert getTypeConvert(DbType dbType) {
        switch (dbType) {
            case MYSQL :
                return new MySqlTypeConvert();
            case POSTGRE_SQL :
                return new PostgreSqlTypeConvert();
            case ORACLE :
                return new OracleTypeConvert();
            case SQL_SERVER:
            case SQL_SERVER2005:
                return  new SqlServerTypeConvert();
        }
        return null;
    }

    public static IKeyWordsHandler getKeyWordsHandler(DbType dbType){
        IKeyWordsHandler keyWordsHandler = null;
        switch (dbType) {
            case MYSQL :
                keyWordsHandler = new MySqlKeyWordsHandler();
                break;
            case POSTGRE_SQL :
                keyWordsHandler = new PostgreSqlKeyWordsHandler();
                break;
            case H2 :
                keyWordsHandler = new H2KeyWordsHandler();
                break;
        }
        return keyWordsHandler;
    }

    /**
     * 格式化数据库注释内容
     *
     * @param comment 注释
     * @return 注释
     * @since 3.4.0
     */
    public static String formatComment(String comment) {
        return StringUtils.isBlank(comment) ? StringPool.EMPTY : comment.replaceAll("\r\n", "\t");
    }

    /**
     *
     * @param componentType
     * @return
     */
    public static String getQueryType(String componentType){
        String resultType = StringPool.EMPTY;
        if (StringUtil.equalsIgnoreCase(componentType, "input")
                || StringUtil.equalsIgnoreCase(componentType, "textarea")
                || StringUtil.equalsIgnoreCase(componentType, "ueditor")) {
            resultType = "like";
        } else if (StringUtil.equalsIgnoreCase(componentType, "datetime")
                || StringUtil.equalsIgnoreCase(componentType, "date")) {
            resultType = "date";
        } else {
            resultType = "equal";
        }
        return resultType;
    }

    public static CodeSchemaDto transAppParamToWeb(AppCodeSchemaDto appCodeSchemaDto) {
        CodeSchemaDto codeSchemaDto = new CodeSchemaDto();
        // 数据库连接id
        DbSettingDto dbSettingDto = appCodeSchemaDto.getDbSettingDto();
        codeSchemaDto.setDbLinkId(dbSettingDto.getDbId());
        // 数据库表关联关系
        codeSchemaDto.setDbTableDtoList(dbSettingDto.getDbTableDtoList());
        // 基础信息
        codeSchemaDto.setBaseInfoDto(appCodeSchemaDto.getBaseInfoDto());
        // 列表配置
        ColDataDto colDataDto = new ColDataDto();
        codeSchemaDto.setColDataDto(colDataDto);
        AppListViewSettingDto listViewSettingDto = appCodeSchemaDto.getListViewSettingDto();
        //// 按钮
        List<AppColumnBtnDto> appColumnBtnDtoList = listViewSettingDto.getColumnBtnDtoList();
        List<Map<String, Object>> colBtnList = new ArrayList<>(appColumnBtnDtoList.size());
        for (AppColumnBtnDto appColumnBtnDto: appColumnBtnDtoList) {
            Map<String, Object> btn = new HashMap<>(4);
            btn.put("name", appColumnBtnDto.getName());
            btn.put("val", appColumnBtnDto.getId());
            btn.put("icon", appColumnBtnDto.getIcon());
            btn.put("checked", appColumnBtnDto.getSelect());
            colBtnList.add(btn);
        }
        colDataDto.setButtonList(colBtnList);
        // 默认分页
        colDataDto.setIsPage("1");
        //// 列表字段
        List<AppListViewColumnDto> listViewColumnDtoList = listViewSettingDto.getListViewColumnDtoList();
        List<Map<String, Object>> columnList = new ArrayList<>(listViewColumnDtoList.size());
        int index = 0;
        for (AppListViewColumnDto listViewColumnDto : listViewColumnDtoList) {
            Map<String, Object> col = new HashMap<>(4);
            col.put("bindColumn", listViewColumnDto.getField());
            col.put("$index", index++);
            col.put("fieldName", listViewColumnDto.getName());
            columnList.add(col);
        }
        colDataDto.setColumnList(columnList);
        // 表单配置
        List<AppFieldConfigDto> appFormDataDtoList = appCodeSchemaDto.getFormDataDtoList();
        List<FormDataDto> formDataDtoList = new ArrayList<>(appFormDataDtoList.size());
        for (AppFieldConfigDto appFieldConfigDto : appFormDataDtoList) {
            FormDataDto formDataDto = new FormDataDto();
            Map<String, Object> config = new HashMap<>(8);
            formDataDto.setConfig(config);
            config.put("label", appFieldConfigDto.getName());
            String type = appFieldConfigDto.getType();
            config.put("avueType", type);
            config.put("bindTable", appFieldConfigDto.getTable());
            if (StringUtil.equalsIgnoreCase("table", type)) {
                AppFieldOptionDto optionDto = appFieldConfigDto.getOptionDto();
                List<AppSubFromFieldDto> subFromFieldList = optionDto.getColumnList();
                List<Map<String, Object>> subTableColList = new ArrayList<>(subFromFieldList.size());
                config.put("children", subTableColList);
                for (AppSubFromFieldDto subFromFieldDto : subFromFieldList) {
                    Map<String, Object> subCol = new HashMap<>(4);
                    Map<String, Object> subConfig = new HashMap<>(16);
                    subCol.put("__config__", subConfig);
                    subConfig.put("label", subFromFieldDto.getLabel());
                    subConfig.put("avueType", subFromFieldDto.getType());
                    subConfig.put("bindTable", appFieldConfigDto.getTable());
                    subConfig.put("bindTableField", subFromFieldDto.getProp());
                    if (StringUtil.equalsIgnoreCase(subFromFieldDto.getDataSource(), "0")) {
                        subConfig.put("dataType", "dataSource");
                        subConfig.put("dataSource", subFromFieldDto.getDataItem());
                        subConfig.put("showField", subFromFieldDto.getShowField());
                        subConfig.put("saveField", subFromFieldDto.getSaveField());

                    } else if (StringUtil.equalsIgnoreCase(subFromFieldDto.getDataSource(), "1")) {
                        subConfig.put("dataType", "dataItem");
                        subConfig.put("dataItem", subFromFieldDto.getDataItem());
                    }
                    subTableColList.add(subCol);
                }
            } else {
                config.put("bindTableField", appFieldConfigDto.getField());
                if (StringUtil.equalsIgnoreCase(appFieldConfigDto.getDataSource(), "0")) {
                    config.put("dataType", "dataSource");
                    config.put("dataSource", appFieldConfigDto.getDataItem());
                    config.put("showField", appFieldConfigDto.getShowField());
                    config.put("saveField", appFieldConfigDto.getSaveField());

                } else if (StringUtil.equalsIgnoreCase(appFieldConfigDto.getDataSource(), "1")) {
                    config.put("dataType", "dataItem");
                    config.put("dataItem", appFieldConfigDto.getDataItem());
                }
            }
            formDataDtoList.add(formDataDto);
        }
        codeSchemaDto.setFormDataDtoList(formDataDtoList);

        // 查询配置
        List<AppQueryFieldDto> queryFieldDtoList = appCodeSchemaDto.getQueryFieldDtoList();
        List<QueryDataDto> queryDataDtoList = new ArrayList<>(queryFieldDtoList.size());
        for (AppQueryFieldDto queryFieldDto : queryFieldDtoList) {
            QueryDataDto queryDataDto = new QueryDataDto();
//            queryDataDto.setDateField();
            queryDataDto.setIsDate(true);
            queryDataDto.setField(queryFieldDto.getField());
            queryDataDto.setName(queryFieldDto.getName());
            queryDataDto.setSearchtype(StringUtil.equalsIgnoreCase("time", queryFieldDto.getType()) ? "数据字段查询" : "时间字段查询");
            queryDataDtoList.add(queryDataDto);
        }
        codeSchemaDto.setQueryDataDtoList(queryDataDtoList);
        return codeSchemaDto;
    }
}
