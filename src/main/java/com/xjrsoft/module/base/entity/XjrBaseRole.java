package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.cache.CacheAble;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_role")
public class XjrBaseRole implements CacheAble,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色主键
     */
    @TableId("F_RoleId")
    @JsonProperty("F_RoleId")
    private String roleId;


    /**
     * 分类
     */
    @TableField("F_Category")
    @JsonProperty("F_Category")
    private String category;

    /**
     * 角色编码
     */
    @TableField("F_EnCode")
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 角色名称
     */
    @TableField("F_FullName")
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    @TableLogic
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @TableField("F_Description")
    @JsonProperty("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    @JsonProperty("F_CreateDate")
    @JsonIgnore
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建用户
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    @JsonProperty("F_ModifyDate")
    @JsonIgnore
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    @JsonProperty("F_ModifyUserId")
    private String modifyUserId;

    /**
     * 修改用户
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    @JsonProperty("F_ModifyUserName")
    private String modifyUserName;


    @Override
    public String getCacheId() {
        return this.roleId;
    }
}
