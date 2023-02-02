package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserSimpleInfoVo {

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
     * 性别
     */
    @JsonProperty("F_Gender")
    private Integer gender;

    /**
     * 手机
     */
    @JsonProperty("F_Mobile")
    private String mobile;
}
