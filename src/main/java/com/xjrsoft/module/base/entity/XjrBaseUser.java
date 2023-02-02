package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.common.cache.CacheAble;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_user")
@Accessors(chain = true)
public class XjrBaseUser implements CacheAble,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    @TableId("F_UserId")
    private String userId;

    /**
     * 工号
     */
    @TableField("F_EnCode")
    private String enCode;

    /**
     * 登录账户
     */
    @TableField("F_Account")
    private String account;

    /**
     * 登录密码
     */
    @TableField("F_Password")
    private String password;

    /**
     * 密码秘钥
     */
    @TableField("F_Secretkey")
    private String secretkey;

    /**
     * 真实姓名
     */
    @TableField("F_RealName")
    private String realName;

    /**
     * 呢称
     */
    @TableField("F_NickName")
    private String nickName;

    /**
     * 头像
     */
    @TableField("F_HeadIcon")
    private String headIcon;

    /**
     * 快速查询
     */
    @TableField("F_QuickQuery")
    private String quickQuery;

    /**
     * 简拼
     */
    @TableField("F_SimpleSpelling")
    private String simpleSpelling;

    /**
     * 性别
     */
    @TableField("F_Gender")
    private Integer gender;

    /**
     * 生日
     */
    @TableField("F_Birthday")
    private LocalDateTime birthday;

    /**
     * 手机
     */
    @TableField("F_Mobile")
    private String mobile;

    /**
     * 电话
     */
    @TableField("F_Telephone")
    private String telephone;

    /**
     * 电子邮件
     */
    @TableField("F_Email")
    private String email;

    /**
     * QQ号
     */
    @TableField("F_OICQ")
    private String oicq;

    /**
     * 微信号
     */
    @TableField("F_WeChat")
    private String weChat;

    /**
     * MSN
     */
    @TableField("F_MSN")
    private String msn;

    /**
     * 机构主键
     */
    @TableField("F_CompanyId")
    private String companyId;

    /**
     * 部门主键 废除
     */
//    @TableField("F_DepartmentId")
//    private String departmentId;

    /**
     * 安全级别
     */
    @TableField("F_SecurityLevel")
    private Integer securityLevel;

    /**
     * 单点登录标识
     */
    @TableField("F_OpenId")
    private Integer openId;

    /**
     * 密码提示问题
     */
    @TableField("F_Question")
    private String question;

    /**
     * 密码提示答案
     */
    @TableField("F_AnswerQuestion")
    private String answerQuestion;

    /**
     * 允许多用户同时登录
     */
    @TableField("F_CheckOnLine")
    private Integer checkOnLine;

    /**
     * 允许登录时间开始
     */
    @TableField("F_AllowStartTime")
    private LocalDateTime allowStartTime;

    /**
     * 允许登录时间结束
     */
    @TableField("F_AllowEndTime")
    private LocalDateTime allowEndTime;

    /**
     * 暂停用户开始日期
     */
    @TableField("F_LockStartDate")
    private LocalDateTime lockStartDate;

    /**
     * 暂停用户结束日期
     */
    @TableField("F_LockEndDate")
    private LocalDateTime lockEndDate;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建用户
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    @JsonIgnore
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改用户
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;

    /**
     * 登录会话ID
     */
    @TableField("F_Token")
    private String token;

    /**
     * 地理位置
     */
    @TableField("F_DingTalkId")
    private String dingTalkId;

    /**
     * 地理位置
     */
    @TableField("F_WeChatId")
    private String weChatId;


    @Override
    public String getCacheId() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "{" +
                "userId='" + userId + '\'' +
                ", enCode='" + enCode + '\'' +
                ", account='" + account + '\'' +
                ", name='" + realName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
