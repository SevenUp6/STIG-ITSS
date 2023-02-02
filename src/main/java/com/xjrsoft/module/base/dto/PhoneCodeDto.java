package com.xjrsoft.module.base.dto;

import lombok.Data;

@Data
public class PhoneCodeDto {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;
}
