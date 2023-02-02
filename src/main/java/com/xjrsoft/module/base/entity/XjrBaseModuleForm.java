package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 功能表单字段
 * </p>
 *
 * @author jobob
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_moduleform")
public class XjrBaseModuleForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列主键
     */
    @TableId("F_ModuleFormId")
    private String moduleFormId;

    /**
     * 功能主键
     */
    @TableField("F_ModuleId")
    private String moduleId;

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

    @TableField("F_IsRequired")
    private Integer isRequired;


}
