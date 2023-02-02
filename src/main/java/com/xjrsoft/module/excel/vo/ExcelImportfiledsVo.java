package com.xjrsoft.module.excel.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
* @Author:光华科技-软件研发部
* @Date:2020/11/10
* @Description:excel导入字段
*/
@Data
public class ExcelImportfiledsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String Id;

    /**
     * 导入模板Id
     */
    @JsonProperty("F_ImportId")
    private String ImportId;

    /**
     * 字典名字
     */
    @JsonProperty("F_Name")
    private String Name;

    /**
     * excel名字
     */
    @JsonProperty("F_ColName")
    private String ColName;

    /**
     * 唯一性验证:0要,1需要
     */
    @JsonProperty("F_OnlyOne")
    private Integer OnlyOne;

    /**
     * 关联类型0:无关联,1:GUID,2:数据字典3:数据表;4:固定数值;5:操作人ID;6:操作人名字;7:操作时间;
     */
    @JsonProperty("F_RelationType")
    private Integer RelationType;

    /**
     * 数据字典编号
     */
    @JsonProperty("F_DataItemCode")
    private String DataItemCode;

    /**
     * 固定数据
     */
    @JsonProperty("F_Value")
    private String Value;

    /**
     * 关联库id
     */
    @JsonProperty("F_DataSourceId")
    private String DataSourceId;

    /**
     * 排序
     */
    @JsonProperty("F_SortCode")
    private Integer SortCode;


}
