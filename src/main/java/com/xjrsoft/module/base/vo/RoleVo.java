package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleVo {
    private static final long serialVersionUID = 1L;

    /**
     * 角色主键
     */
    @JsonProperty("F_RoleId")
    private String roleId;

    /**
     * 分类
     */
    @JsonProperty("F_Category")
    private String category;

    /**
     * 角色编码
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 角色名称
     */
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建用户
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    /**
     * 修改日期
     */
    @JsonProperty("F_ModifyDate")
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @JsonProperty("F_ModifyUserId")
    private String modifyUserId;

    /**
     * 修改用户
     */
    @JsonProperty("F_ModifyUserName")
    private String modifyUserName;
}
