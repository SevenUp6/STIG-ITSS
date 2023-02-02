package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户常用功能表
 * </p>
 *
 * @author jobob
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_commonmodule")
public class XjrBaseCommonModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 用户主键
     */
    @TableField("F_UserId")
    private String userId;

    /**
     * 系统功能主键
     */
    @TableField("F_ModuleId")
    private String moduleId;

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
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;


}
