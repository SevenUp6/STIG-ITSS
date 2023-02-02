package com.xjrsoft.module.base.dto;


import lombok.Data;

import java.util.List;

@Data
public class WeChatUserDto {
    //成员UserID
    private String userid;

    //成员名称
    private String name;

    //手机号码
    private String mobile;


    //所属部门
    private List<Integer> department;


    //性别。0表示未定义，1表示男性，2表示女性
    private String gender;


    //邮箱
    private String email;

    //头像url
    private String avatar;
    
    //地址
    private String address;

}
