package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_module")
public class XjrBaseModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("F_ModuleId")
    private String moduleId;

    @TableField("F_ParentId")
    private String parentId;

    @TableField("F_EnCode")
    private String enCode;

    @TableField("F_FullName")
    private String fullName;

    @TableField("F_Icon")
    private String icon;

    @TableField("F_Component")
    private String component;

    @TableField("F_UrlAddress")
    private String urlAddress;

    @TableField("F_Target")
    private String target;

    @TableField("F_IsMenu")
    private Integer isMenu;

    @TableField("F_AllowExpand")
    private Integer allowExpand;

    @TableField("F_IsPublic")
    private Integer isPublic;

    @TableField("F_AllowEdit")
    private Integer allowEdit;

    @TableField("F_AllowDelete")
    private Integer allowDelete;

    @TableField("F_SortCode")
    private Integer sortCode;

    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteMark;

    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

    @TableField(value = "F_SubSystemId", fill = FieldFill.INSERT)
    private String subSystemId;

    @TableField("F_Description")
    private String description;

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

    @Override
    public String toString() {
        return "XjrBaseModule{" +
                "moduleId='" + moduleId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", enCode='" + enCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", icon='" + icon + '\'' +
                ", component='" + component + '\'' +
                ", urlAddress='" + urlAddress + '\'' +
                ", target='" + target + '\'' +
                ", isMenu=" + isMenu +
                ", allowExpand=" + allowExpand +
                ", isPublic=" + isPublic +
                ", allowEdit=" + allowEdit +
                ", allowDelete=" + allowDelete +
                ", sortCode=" + sortCode +
                ", deleteMark=" + deleteMark +
                ", enabledMark=" + enabledMark +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", createUserId='" + createUserId + '\'' +
                ", createUserName='" + createUserName + '\'' +
                ", modifyDate=" + modifyDate +
                ", modifyUserId='" + modifyUserId + '\'' +
                ", modifyUserName='" + modifyUserName + '\'' +
                '}';
    }
}
