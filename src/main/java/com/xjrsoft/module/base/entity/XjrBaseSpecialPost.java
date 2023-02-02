package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_special_post")
public class XjrBaseSpecialPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("F_Id")
    private String id;

    @TableField("F_Name")
    private String name;

    @TableField("F_Remark")
    private String remark;

    @TableField("F_Sort")
    private Integer sort;

    @TableField("F_Type")
    private Integer type;

    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    private LocalDateTime modifyDate;

    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;

    @TableField("F_Temp1")
    private String temp1;

    @TableField("F_Temp2")
    private String temp2;


}
