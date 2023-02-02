package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 代码模板
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_codeschema")
public class XjrBaseCodeschema implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 模板名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 模板类型
     */
    @TableField("F_Type")
    private String type;

    /**
     * 行业类型
     */
    @TableField("F_Catalog")
    private String catalog;

    /**
     * 代码Schema
     */
    @TableField("F_CodeSchema")
    private String codeSchema;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建人ID
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人姓名
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;


}
