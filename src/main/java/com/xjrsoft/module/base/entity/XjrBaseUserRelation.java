package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.common.cache.CacheAble;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户关系表
 * </p>
 *
 * @author jobob
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_userrelation")
@Accessors(chain = true)
public class XjrBaseUserRelation implements CacheAble {

    private static final long serialVersionUID = 1L;

    /**
     * 用户关系主键
     */
    @TableId("F_UserRelationId")
    private String userRelationId;

    /**
     * 用户主键
     */
    @TableField("F_UserId")
    private String userId;

    /**
     * 分类:1-角色2-岗位3-部门
     */
    @TableField("F_Category")
    private Integer category;

    /**
     * 对象主键
     */
    @TableField("F_ObjectId")
    private String objectId;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    @JsonIgnore
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

    @Override
    public String getCacheId() {
        return this.userRelationId;
    }
}
