package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 功能按钮表
 * </p>
 *
 * @author jobob
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_modulebutton")
public class XjrBaseModuleButton implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 按钮主键
     */
    @TableId("F_ModuleButtonId")
    private String moduleButtonId;

    /**
     * 功能主键
     */
    @TableField("F_ModuleId")
    private String moduleId;

    /**
     * 父级主键
     */
    @TableField("F_ParentId")
    private String parentId;

    /**
     * 图标
     */
    @TableField("F_Icon")
    private String icon;

    /**
     * 编码
     */
    @TableField("F_EnCode")
    private String enCode;

    /**
     * 名称
     */
    @TableField("F_FullName")
    private String fullName;

    /**
     * Action地址
     */
    @TableField("F_ActionAddress")
    private String actionAddress;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    @TableField("F_ActionName")
    private String actionName;


}
