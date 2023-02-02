package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zwq
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_draft")
public class XjrBaseDraft implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("F_Id")
    private String id;

    /**
     * 模板id
     */
    @TableField("F_SchemeinfoId")
    private String schemeinfoId;

    /**
     * 模板名
     */
    @TableField("F_SchemeinfoName")
    private String schemeinfoName;

    /**
     * 表单数据
     */
    @TableField("F_Value")
    private String value;

    /**
     * 创建时间
     */
    @TableField("F_CreateDate")
    private Date createDate;

    /**
     * 创建者id
     */
    @TableField("F_CreateUserId")
    private String createUserId;

    /**
     * 创建用户名
     */
    @TableField("F_CreateUserName")
    private String createUserName;

    /**
     * 删除标记
     */
    @TableField("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 修改日期
     */
    @TableField("F_ModifyDate")
    private Date modifyDate;

    /**
     * 修改用户id
     */
    @TableField("F_ModifyUserId")
    private String modifyUserId;

    /**
     * 修改用户名字
     */
    @TableField("F_ModifyUserName")
    private String modifyUserName;


}
