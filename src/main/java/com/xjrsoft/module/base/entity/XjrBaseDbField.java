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
 * 建表常用字段
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_dbfield")
public class XjrBaseDbField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("F_Id")
    private String id;

    /**
     * 字段名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 数据类型
     */
    @TableField("F_DataType")
    private String dataType;

    /**
     * 说明
     */
    @TableField("F_Remark")
    private String remark;

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
     * 创建人
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    @TableField("F_Length")
    private Integer length;


}
