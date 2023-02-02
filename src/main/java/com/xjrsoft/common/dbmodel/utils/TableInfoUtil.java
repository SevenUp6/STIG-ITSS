package com.xjrsoft.common.dbmodel.utils;

import com.xjrsoft.common.dbmodel.entity.TableField;
import com.xjrsoft.common.dbmodel.entity.TableInfo;
import com.xjrsoft.common.dbmodel.entity.TableRelation;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.buildCode.dto.DbTableDto;
import com.xjrsoft.module.buildCode.dto.TableFieldDto;
import com.xjrsoft.module.buildCode.dto.TableInfoDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableInfoUtil {
    private TableInfoUtil(){}

    public static TableInfo addComFieldAndKey(TableInfo tableInfo) {
        List<TableField> fieldList = new ArrayList<>();
        // 主键 第一位
        TableField id = new TableField();
        id.setName("F_Id");
        id.setType("varchar");
        id.setIsKey(true);
        id.setComment("主键值");
        id.setLength(50);
        fieldList.add(id);

        List<TableField> fields = tableInfo.getFields();
        if (CollectionUtil.isNotEmpty(fields)) {
            fieldList.addAll(fields);
        }

        TableField deleteMark = new TableField();
        deleteMark.setName("F_DeleteMark");
        deleteMark.setType("int");
        deleteMark.setComment("删除标记");
        deleteMark.setLength(11);
        fieldList.add(deleteMark);

        TableField enabledMark = new TableField();
        enabledMark.setName("F_EnabledMark");
        enabledMark.setType("int");
        enabledMark.setComment("有效标记");
        enabledMark.setLength(11);
        fieldList.add(enabledMark);

        TableField createdDate = new TableField();
        createdDate.setName("F_CreateDate");
        createdDate.setType("datetime");
        createdDate.setComment("创建时间");
        fieldList.add(createdDate);

        TableField createdUserId = new TableField();
        createdUserId.setName("F_CreateUserId");
        createdUserId.setType("varchar");
        createdUserId.setComment("创建人主键值");
        createdUserId.setLength(50);
        fieldList.add(createdUserId);

        TableField createdUserName = new TableField();
        createdUserName.setName("F_CreateUserName");
        createdUserName.setType("varchar");
        createdUserName.setComment("创建人姓名");
        createdUserName.setLength(50);
        fieldList.add(createdUserName);

        TableField modifyDate = new TableField();
        modifyDate.setName("F_ModifyDate");
        modifyDate.setType("datetime");
        modifyDate.setComment("修改时间");
        fieldList.add(modifyDate);

        TableField modifyUserId = new TableField();
        modifyUserId.setName("F_ModifyUserId");
        modifyUserId.setType("varchar");
        modifyUserId.setComment("修改人主键值");
        modifyUserId.setLength(50);
        fieldList.add(modifyUserId);

        TableField modifyUserName = new TableField();
        modifyUserName.setName("F_ModifyUserName");
        modifyUserName.setType("varchar");
        modifyUserName.setComment("修改人姓名");
        modifyUserName.setLength(50);
        fieldList.add(modifyUserName);

        tableInfo.setFields(fieldList);
        return tableInfo;
    }

    /**
     * @param tableRelationList 表关联关系对象集合
     * @param tableInfoList 表配置信息对象集合
     */
    public static void buildRelationField(List<DbTableDto> tableRelationList, List<TableInfoDto> tableInfoList) {
        if (CollectionUtil.isNotEmpty(tableRelationList) && tableRelationList.size() > 1) {
            String mainTableName = StringPool.EMPTY;
            Map<String, String> tableRelationFiledNameMap = new HashMap<>(tableInfoList.size() - 1);
            for (DbTableDto tableRelation: tableRelationList) {
                if (!tableRelation.isMainTable()) {
                    tableRelationFiledNameMap.put(tableRelation.getName(), tableRelation.getField());
                } else {
                    mainTableName = tableRelation.getName();
                }
            }
            for (TableInfoDto tableInfo : tableInfoList) {
                String relationFieldName = tableRelationFiledNameMap.get(tableInfo.getName());
                List<TableFieldDto> fields = tableInfo.getFields();
                if(!StringUtil.isEmpty(relationFieldName)) {
                    TableFieldDto relationField = new TableFieldDto();
                    relationField.setLength(50);
                    relationField.setComment(mainTableName + "主键值");
                    relationField.setType("varchar");
                    relationField.setName(relationFieldName);
                    fields.add(relationField);
                }
                // 检验是否有重复的字段
                List<String> fieldNameList = new ArrayList<>(fields.size());
                for (TableFieldDto field : fields) {
                    String name = field.getName();
                    if (fieldNameList.contains(name)) {
                        throw new RuntimeException("字段名重复：" + name);
                    }
                    fieldNameList.add(StringUtils.lowerCase(name));
                }
            }
        }
    }
}
