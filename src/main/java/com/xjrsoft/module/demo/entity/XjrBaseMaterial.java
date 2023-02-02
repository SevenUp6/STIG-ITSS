package com.xjrsoft.module.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 物料表
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_material")
public class XjrBaseMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 物料名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 折扣
     */
    @TableField("F_Price")
    private Double price;


    /**
     * 数量
     */
    @TableField("F_Count")
    private Integer count;

    /**
     * 折扣
     */
    @TableField("F_Discount")
    private Double discount;

    /**
     * 最低高度
     */
    @TableField("F_MinHeight")
    private Double minHeight;

    /**
     * 最高高度
     */
    @TableField("F_MaxHeight")
    private Double maxHeight;

    /**
     * 删除标记（0-未删除，1-已删除）
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    private String deleteMark;

    /**
     * 启用标记（0-不启用，1-启用）
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private String enabledMark;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private String createDate;

    /**
     * 创建人主键值
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人名称
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;
}
