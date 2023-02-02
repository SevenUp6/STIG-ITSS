package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_log")
public class XjrBaseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId("F_LogId")
    private String logId;

    /**
     * 分类Id 1-登陆2-访问3-操作4-异常
     */
    @TableField("F_CategoryId")
    private Integer categoryId;

    /**
     * 来源对象主键
     */
    @TableField("F_SourceObjectId")
    private String sourceObjectId;

    /**
     * 来源日志内容
     */
    @TableField("F_SourceContentJson")
    private String sourceContentJson;

    /**
     * 操作时间
     */
    @TableField("F_OperateTime")
    private LocalDateTime operateTime;

    /**
     * 操作用户Id
     */
    @TableField("F_OperateUserId")
    private String operateUserId;

    /**
     * 操作用户
     */
    @TableField("F_OperateAccount")
    private String operateAccount;

    /**
     * 操作类型Id
     */
    @TableField("F_OperateTypeId")
    private String operateTypeId;

    /**
     * 操作类型
     */
    @TableField("F_OperateType")
    private String operateType;

    /**
     * 系统功能
     */
    @TableField("F_Module")
    private String module;

    /**
     * IP地址
     */
    @TableField("F_IPAddress")
    private String ipAddress;

    /**
     * IP地址所在城市
     */
    @TableField("F_IPAddressName")
    private String ipAddressName;

    /**
     * 主机
     */
    @TableField("F_Host")
    private String host;

    /**
     * 浏览器
     */
    @TableField("F_Browser")
    private String browser;

    /**
     * 执行结果状态
     */
    @TableField("F_ExecuteResult")
    private Integer executeResult;

    /**
     * 执行结果信息
     */
    @TableField("F_ExecuteResultJson")
    private String executeResultJson;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

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


}
