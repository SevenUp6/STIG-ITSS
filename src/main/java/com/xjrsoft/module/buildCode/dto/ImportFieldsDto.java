package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "ExcelImportFieldsDto", description = "excelFields导入dto")
public class ImportFieldsDto {

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 导入模板Id
     */
    @JsonProperty("F_ImportId")
    private String importId;

    /**
     * 字典名字
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * excel名字
     */
    @JsonProperty("F_ColName")
    private String colName;

    /**
     * 唯一性验证:0要,1需要
     */
    @JsonProperty("F_OnlyOne")
    private Integer onlyOne;

    /**
     * 关联类型0:无关联,1:GUID,2:数据字典3:数据表;4:固定数值;5:操作人ID;6:操作人名字;7:操作时间;
     */
    @JsonProperty("F_RelationType")
    private Integer relationType;

    /**
     * 数据字典编号
     */
    @JsonProperty("F_DataItemCode")
    private String dataItemCode;

    /**
     * 固定数据
     */
    @JsonProperty("F_Value")
    private String value;

    /**
     * 关联库id
     */
    @JsonProperty("F_DataSourceId")
    private String dataSourceId;

    /**
     * 排序
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;
}
