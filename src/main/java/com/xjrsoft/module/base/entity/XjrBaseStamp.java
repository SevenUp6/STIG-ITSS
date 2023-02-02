package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_stamp")
public class XjrBaseStamp implements Serializable {

    @TableId("F_StampId")
    private String stampId;

    @TableField("F_StampName")
    private String stampName;

    @TableField("F_Description")
    private String description;

    @TableField("F_StampType")
    private String stampType;

    @TableField("F_Password")
    private String password;

    @TableField("F_ImgFile")
    private String imgFile;

    @TableField("F_Sort")
    private String sort;

    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

    @TableField("F_File_Type")
    private Byte fileType;

    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private Date createDate;

    /**
    * 0:普通签章 1：默认签章 2：公共签章
    */
    @TableField("F_StampAttributes")
    private Integer stampAttributes;


    @TableField("F_AuthorizeUser")
    private String authorizeUser;


    //成员
    @TableField("F_Member")
    private String member;

    //维护人员
    @TableField("F_Maintain")
    private String maintain;

    private static final long serialVersionUID = 1L;

}