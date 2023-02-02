package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据字典分类表
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_dataitem")
public class XjrBaseDataItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类主键
     */
    @TableId("F_ItemId")
    private String itemId;

    /**
     * 父级主键
     */
    @TableField("F_ParentId")
    private String parentId;

    /**
     * 分类编码
     */
    @TableField("F_ItemCode")
    private String itemCode;

    /**
     * 分类名称
     */
    @TableField("F_ItemName")
    private String itemName;

    /**
     * 树型结构
     */
    @TableField("F_IsTree")
    private Integer isTree;

    /**
     * 导航标记
     */
    @TableField("F_IsNav")
    private Integer isNav;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    @TableLogic
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
}
