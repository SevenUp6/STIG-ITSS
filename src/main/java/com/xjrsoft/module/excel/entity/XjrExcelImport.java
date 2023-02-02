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
* @Author:光华科技-软件研发部
* @Date:2020/11/10
* @Description:excel导入实体类
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_excel_import")
public class XjrExcelImport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 关联模块Id
     */
    @TableField("F_ModuleId")
    private String moduleId;

    /**
     * 关联按钮Id
     */
    @TableField("F_ModuleBtnId")
    private String moduleBtnId;

    /**
     * 按钮名称
     */
    @TableField("F_BtnName")
    private String btnName;

    /**
     * 导入数据库ID
     */
    @TableField("F_DbId")
    private String dbId;

    /**
     * 导入数据库表
     */
    @TableField("F_DbTable")
    private String dbTable;

    /**
     * 错误处理机制0终止,1跳过
     */
    @TableField("F_ErrorType")
    private Integer errorType;

    /**
     * 是否有效0暂停,1启用
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建人Id
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人名字
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 修改时间
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
