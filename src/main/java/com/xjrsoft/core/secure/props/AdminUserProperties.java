package com.xjrsoft.core.secure.props;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("xjrsoft.global-config.admin-user-info")
public class AdminUserProperties {

    /**
     * 用户主键
     */
    @TableId("F_UserId")
    private String userId = "admin";

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
     * 真实姓名
     */
    @TableField("F_RealName")
    private String realName = "超级管理员";
}
