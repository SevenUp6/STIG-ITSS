package com.xjrsoft.module.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class LoginInfoDto {

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "工号")
    private String enCode;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "授权类型")
    private String grantType;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;
}
