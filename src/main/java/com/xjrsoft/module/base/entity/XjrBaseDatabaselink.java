package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据库连接表
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_databaselink")
public class XjrBaseDatabaselink implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库连接主键
     */
    @TableId("F_DatabaseLinkId")
    private String databaseLinkId;

    /**
     * 服务器地址
     */
    @TableField("F_ServerAddress")
    private String serverAddress;

    /**
     * 数据库名称
     */
    @TableField("F_DBName")
    private String dbName;

    /**
     * 数据库别名
     */
    @TableField("F_DBAlias")
    private String dbAlias;

    /**
     * 数据库类型
     */
    @TableField("F_DbType")
    private String dbType;

    /**
     * 数据库版本
     */
    @TableField("F_Version")
    private String version;

    /**
     * 连接地址
     */
    @TableField("F_DbConnection")
    private String dbConnection;

    /**
     * 连接地址是否加密
     */
    @TableField("F_DESEncrypt")
    private Integer deseNcrypt;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark",fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @TableField(value = "F_EnabledMark",fill = FieldFill.INSERT)
    private Integer enabledMark;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate",fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @TableField(value = "F_CreateUserId",fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建用户
     */
    @TableField(value = "F_CreateUserName",fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate",fill = FieldFill.UPDATE)
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @TableField(value = "F_ModifyUserId",fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改用户
     */
    @TableField(value = "F_ModifyUserName",fill = FieldFill.UPDATE)
    private String modifyUserName;

    /**
     * 连接数据库密码
     */
    @TableField("F_DBPwd")
    private String dbPwd;

    /**
     * 连接数据库用户名
     */
    @TableField("F_DBUserName")
    private String dbUserName;


}
