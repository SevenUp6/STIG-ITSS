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
 * 数据表草稿
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_dbdraft")
public class XjrBaseDbDraft implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("F_Id")
    private String id;

    /**
     * 表名
     */
    @TableField("F_Name")
    private String name;

    /**
     * 内容
     */
    @TableField("F_Content")
    private String content;

    /**
     * 表备注
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
     * 创建人姓名
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;
}
