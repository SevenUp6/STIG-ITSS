package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDto {
    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    @JsonProperty("F_UserId")
    private String userId;

    /**
     * 工号
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 登录账户
     */
    @JsonProperty("F_Account")
    private String account;

    /**
     * 登录密码
     */
    @JsonProperty("F_Password")
    private String password;

    /**
     * 密码秘钥
     */
    @JsonProperty("F_Secretkey")
    private String secretkey;

    /**
     * 真实姓名
     */
    @JsonProperty("F_RealName")
    private String realName;

    /**
     * 呢称
     */
    @JsonProperty("F_NickName")
    private String nickName;

    /**
     * 头像
     */
    @JsonProperty("F_HeadIcon")
    private String headIcon;

    /**
     * 快速查询
     */
    @JsonProperty("F_QuickQuery")
    private String quickQuery;

    /**
     * 简拼
     */
    @JsonProperty("F_SimpleSpelling")
    private String simpleSpelling;

    /**
     * 性别
     */
    @JsonProperty("F_Gender")
    private Integer gender;

    /**
     * 生日
     */
    @JsonProperty("F_Birthday")
    private LocalDateTime birthday;

    /**
     * 手机
     */
    @JsonProperty("F_Mobile")
    private String mobile;

    /**
     * 电话
     */
    @JsonProperty("F_Telephone")
    private String telephone;

    /**
     * 电子邮件
     */
    @JsonProperty("F_Email")
    private String email;

    /**
     * QQ号
     */
    @JsonProperty("F_OICQ")
    private String oicq;

    /**
     * 微信号
     */
    @JsonProperty("F_WeChat")
    private String weChat;

    /**
     * MSN
     */
    @JsonProperty("F_MSN")
    private String msn;

    /**
     * 机构主键
     */
    @JsonProperty("F_CompanyId")
    private String companyId;

    @JsonProperty("F_CompanyName")
    private String companyName;

    /**
     * 部门主键 废除
     */
    @JsonProperty("F_DepartmentId")
    private String departmentId;

    @JsonProperty("F_DepartmentName")
    private String departmentName;

    /**
     * 允许登录时间开始
     */
    @JsonProperty("F_AllowStartTime")
    private LocalDateTime allowStartTime;

    /**
     * 允许登录时间结束
     */
    @JsonProperty("F_AllowEndTime")
    private LocalDateTime allowEndTime;

    /**
     * 暂停用户开始日期
     */
    @JsonProperty("F_LockStartDate")
    private Date lockStartDate;

    /**
     * 暂停用户结束日期
     */
    @JsonProperty("F_LockEndDate")
    private LocalDateTime lockEndDate;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 有效标志
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
