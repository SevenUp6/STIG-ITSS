package com.xjrsoft.module.login.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.module.base.vo.CompanyPageListVo;
import com.xjrsoft.module.base.vo.DepartmentTreeVo;
import com.xjrsoft.module.base.vo.RoleVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    @JsonProperty("F_UserId")
    private String userId;

    /**
     * 用户账号
     */
    @JsonProperty("F_Account")
    private String account;

    /**
     * 用户姓名
     */
    @JsonProperty("F_RealName")
    private String realName;

    /**
     * 用户头像
     */
    @JsonProperty("F_HeadIcon")
    private String headIcon;

    /**
     * 手机号码
     */
    @JsonProperty("F_Mobile")
    private String mobile;

    /**
     * 电子邮件
     */
    @JsonProperty("F_Email")
    private String email;

    /**
     * 微信号
     */
    @JsonProperty("F_WeChat")
    private String weChat;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    /**
     * 公司
     */
    @JsonProperty("F_Company")
    private CompanyPageListVo company;

    /**
     * 所有部门
     */
    @JsonProperty("F_Department")
    private List<DepartmentTreeVo> departments;

    /**
     * 所有角色
     */
    @JsonProperty("F_Role")
    private List<RoleVo> roles;

    /**
     * 钉钉id
     */
    @JsonProperty("F_DingTalkId")
    private String dingtalkid;

    /**
     * 钉工号
     */
    @JsonProperty("F_EnCode")
    private String enCode;
}
