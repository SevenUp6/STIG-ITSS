package com.xjrsoft.module.excel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
* @Author:湘北智造-框架开发组
* @Date:2020/11/10
* @Description:excel导入字段
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_excel_importfileds")
public class XjrExcelImportfileds implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 导入模板Id
     */
    @TableField("F_ImportId")
    private String importId;

    /**
     * 字典名字
     */
    @TableField("F_Name")
    private String name;

    /**
     * excel名字
     */
    @TableField("F_ColName")
    private String colName;

    /**
     * 唯一性验证:0要,1需要
     */
    @TableField("F_OnlyOne")
    private Integer onlyOne;

    /**
     * 关联类型0:无关联,1:GUID,2:数据字典3:数据表;4:固定数值;5:操作人ID;6:操作人名字;7:操作时间;
     */
    @TableField("F_RelationType")
    private Integer relationType;

    /**
     * 数据字典编号
     */
    @TableField("F_DataItemCode")
    private String dataItemCode;

    /**
     * 固定数据
     */
    @TableField("F_Value")
    private String value;

    /**
     * 关联库id
     */
    @TableField("F_DataSourceId")
    private String dataSourceId;

    /**
     * 排序
     */
    @TableField("F_SortCode")
    private Integer sortCode;


}
