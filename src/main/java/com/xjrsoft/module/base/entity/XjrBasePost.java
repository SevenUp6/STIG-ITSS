package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.common.cache.CacheAble;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author jobob
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_post")
public class XjrBasePost implements CacheAble,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_PostId")
    private String postId;

    /**
     * 上级Id
     */
    @TableField("F_ParentId")
    private String parentId;

    /**
     * 岗位名称
     */
    @TableField("F_Name")
    private String name;

    @TableField("F_EnCode")
    private String enCode;

    /**
     * 公司主键
     */
    @TableField("F_CompanyId")
    private String companyId;

    /**
     * 部门主键
     */
    @TableField("F_DepartmentId")
    private String departmentId;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteMark;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

    /**
     * 创建日期
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

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    @JsonIgnore
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改用户
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;


    @Override
    public String getCacheId() {
        return this.postId;
    }
}
