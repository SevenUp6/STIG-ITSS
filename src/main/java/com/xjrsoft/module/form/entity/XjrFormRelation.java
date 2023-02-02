package com.xjrsoft.module.form.entity;

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
 * 表单-菜单关联关系表
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_form_relation")
public class XjrFormRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 表单主键
     */
    @TableField("F_FormId")
    private String formId;

    /**
     * 功能主键
     */
    @TableField("F_ModuleId")
    private String moduleId;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建人Id
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人名称
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 更新时间
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    private LocalDateTime modifyDate;

    /**
     * 更新人id
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 更新人名称
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;

    /**
     * 发布的设置信息
     */
    @TableField("F_SettingJson")
    private String settingJson;
}
