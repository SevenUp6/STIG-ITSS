package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 功能表格列表
 * </p>
 *
 * @author jobob
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_modulecolumn")
public class XjrBaseModuleColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列主键
     */
    @TableId("F_ModuleColumnId")
    private String moduleColumnId;

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
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;


}
