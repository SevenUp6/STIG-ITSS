package com.xjrsoft.module.sync.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImportUser {

    /**
     * 工号
     */
    private String enCode;

    /**
     * 登录账户
     */
    private String account;


    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 电子邮件
     */
    private String email;


    /**
     * 微信号
     */
    private String weChat;

    /**
     * 部门主键 废除
     */
    private String departmentId;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建日期
     */
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    private String createUserId;

    /**
     * 创建用户
     */
    private String createUserName;
}
