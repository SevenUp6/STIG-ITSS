package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogPageListVo {
    /**
     * 日志主键
     */
    @JsonProperty("F_LogId")
    private String logId;

    /**
     * 分类Id 1-登陆2-访问3-操作4-异常
     */
    @JsonProperty("F_CategoryId")
    private Integer categoryId;

    /**
     * 来源对象主键
     */
    @JsonProperty("F_SourceObjectId")
    private String sourceObjectId;

    /**
     * 来源日志内容
     */
    @JsonProperty("F_SourceContentJson")
    private String sourceContentJson;

    /**
     * 操作时间
     */
    @JsonProperty("F_OperateTime")
    private LocalDateTime operateTime;

    /**
     * 操作用户Id
     */
    @JsonProperty("F_OperateUserId")
    private String operateUserId;

    /**
     * 操作用户
     */
    @JsonProperty("F_OperateAccount")
    private String operateAccount;

    /**
     * 操作类型Id
     */
    @JsonProperty("F_OperateTypeId")
    private String operateTypeId;

    /**
     * 操作类型
     */
    @JsonProperty("F_OperateType")
    private String operateType;

    /**
     * 系统功能
     */
    @JsonProperty("F_Module")
    private String module;

    /**
     * IP地址
     */
    @JsonProperty("F_IPAddress")
    private String ipAddress;

    /**
     * IP地址所在城市
     */
    @JsonProperty("F_IPAddressName")
    private String ipAddressName;

    /**
     * 主机
     */
    @JsonProperty("F_Host")
    private String host;

    /**
     * 浏览器
     */
    @JsonProperty("F_Browser")
    private String browser;

    /**
     * 执行结果状态
     */
    @JsonProperty("F_ExecuteResult")
    private Integer executeResult;

    /**
     * 执行结果信息
     */
    @JsonProperty("F_ExecuteResultJson")
    private String executeResultJson;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
