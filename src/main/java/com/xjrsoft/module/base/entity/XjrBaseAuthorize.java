package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 授权功能表
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_authorize")
public class XjrBaseAuthorize implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 授权功能主键
     */
    @TableId("F_AuthorizeId")
    private String authorizeId;

    /**
     * 对象分类:1-角色2-用户
     */
    @TableField("F_ObjectType")
    private Integer objectType;

    /**
     * 对象主键
     */
    @TableField("F_ObjectId")
    private String objectId;

    /**
     * 项目类型:1-菜单2-按钮3-视图4-表单5-app功能
     */
    @TableField("F_ItemType")
    private Integer itemType;

    /**
     * 项目主键
     */
    @TableField("F_ItemId")
    private String itemId;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建用户
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;


}
