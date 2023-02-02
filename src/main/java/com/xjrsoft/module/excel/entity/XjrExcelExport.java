package com.xjrsoft.module.excel.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author:湘北智造-框架开发组
 * @Date:2020/11/10
 * @Description:excel导出实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_excel_export")
public class XjrExcelExport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId("F_Id")
    private String id;

    /**
     * 文件名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 绑定的JQgirdId
     */
    @TableField("F_GridId")
    private String gridId;

    /**
     * 功能模块Id
     */
    @TableField("F_ModuleId")
    private String moduleId;

    /**
     * 按钮Id
     */
    @TableField("F_ModuleBtnId")
    private String moduleBtnId;

    /**
     * 按钮名称
     */
    @TableField("F_BtnName")
    private String btnName;

    /**
     * 是否有效
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建人ID
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人姓名
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    private LocalDateTime modifyDate;

    /**
     * 修改人Id
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改人名称
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;


}
