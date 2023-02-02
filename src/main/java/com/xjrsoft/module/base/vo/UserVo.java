package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class UserVo {
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

    public String getDingTalkId() {
        return dingTalkId;
    }

    public void setDingTalkId(String dingTalkId) {
        this.dingTalkId = dingTalkId;
    }

    @JsonProperty("F_DingTalkId")
    private String dingTalkId;
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
    private Date birthday;

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

    /**
     * 安全级别
     */
    @JsonProperty("F_SecurityLevel")
    private Integer securityLevel;

    /**
     * 单点登录标识
     */
    @JsonProperty("F_OpenId")
    private Integer openId;

    /**
     * 密码提示问题
     */
    @JsonProperty("F_Question")
    private String question;

    /**
     * 密码提示答案
     */
    @JsonProperty("F_AnswerQuestion")
    private String answerQuestion;

    /**
     * 允许多用户同时登录
     */
    @JsonProperty("F_CheckOnLine")
    private Integer checkOnLine;

    /**
     * 允许登录时间开始
     */
    @JsonProperty("F_AllowStartTime")
    private Date allowStartTime;

    /**
     * 允许登录时间结束
     */
    @JsonProperty("F_AllowEndTime")
    private Date allowEndTime;

    /**
     * 暂停用户开始日期
     */
    @JsonProperty("F_LockStartDate")
    private Date lockStartDate;

    /**
     * 暂停用户结束日期
     */
    @JsonProperty("F_LockEndDate")
    private Date lockEndDate;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

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

    /**
     * 登录会话ID
     */
    @JsonProperty("F_Token")
    private String token;

    @JsonIgnore
    private List<DepartmentSimpleVo> departmentSimpleVoList;

    @JsonProperty("F_DepartmentId")
    private String departmentId;

    @JsonProperty("F_DepartmentName")
    private String departmentName;

    @JsonProperty("F_CreateUserId")
    private String createUserId;

    protected void buildDepartmentParams() {
        StringBuilder departmentIdSb = new StringBuilder();
        StringBuilder departmentNameSb = new StringBuilder();
        if (CollectionUtil.isNotEmpty(this.departmentSimpleVoList)) {
            for (DepartmentSimpleVo departmentSimpleVo : this.departmentSimpleVoList) {
                if (departmentIdSb.length() > 0) {
                    departmentIdSb.append(StringPool.COMMA);
                }
                if (departmentNameSb.length() > 0) {
                    departmentNameSb.append(StringPool.COMMA);
                }
                String departmentId = departmentSimpleVo.getDepartmentId();
                String departmentName = departmentSimpleVo.getDepartmentName();
                if (!StringUtil.isEmpty(departmentId)) departmentIdSb.append(departmentId);
                if (!StringUtil.isEmpty(departmentName)) departmentNameSb.append(departmentName);
            }
            this.departmentId = departmentIdSb.toString();
            this.departmentName = departmentNameSb.toString();
        }
    }

    public String getDepartmentId() {
        if (StringUtils.isEmpty(this.departmentId)) {
            buildDepartmentParams();
        }
        return this.departmentId;
    }

    public String getDepartmentName() {
        if (StringUtils.isEmpty(this.departmentName)) {
            buildDepartmentParams();
        }
        return this.departmentName;
    }
}
