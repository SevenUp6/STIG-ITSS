package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.common.cache.CacheAble;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门信息表
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_department")
@Accessors(chain = true)
public class XjrBaseDepartment implements CacheAble,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门主键
     */
    @TableId("F_DepartmentId")
    private String departmentId;

    /**
     * 公司主键
     */
    @TableField("F_CompanyId")
    private String companyId;

    /**
     * 父级主键
     */
    @TableField("F_ParentId")
    private String parentId;

    /**
     * 部门代码
     */
    @TableField("F_EnCode")
    private String enCode;

    /**
     * 部门名称
     */
    @TableField("F_FullName")
    private String fullName;

    /**
     * 部门简称
     */
    @TableField("F_ShortName")
    private String shortName;

    /**
     * 部门类型
     */
    @TableField("F_Nature")
    private String nature;

    /**
     * 负责人
     */
    @TableField("F_Manager")
    private String manager;

    /**
     * 外线电话
     */
    @TableField("F_OuterPhone")
    private String outerPhone;

    /**
     * 内线电话
     */
    @TableField("F_InnerPhone")
    private String innerPhone;

    /**
     * 电子邮件
     */
    @TableField("F_Email")
    private String email;

    /**
     * 部门传真
     */
    @TableField("F_Fax")
    private String fax;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

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

    /**
     * 钉钉部门ID
     */
    @TableField("F_DingTalkId")
    private String dingTalkId;

    @Override
    public String getCacheId() {
        return this.departmentId;
    }
}
