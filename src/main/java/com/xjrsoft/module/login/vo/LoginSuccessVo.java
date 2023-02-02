package com.xjrsoft.module.login.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LoginSuccessVo {

    /**
     * token
     */
    @JsonProperty("Token")
    private String token;

    /**
     * 用户信息
     */
    @JsonProperty("UserInfo")
    private UserInfoVo userInfoVo;
}
