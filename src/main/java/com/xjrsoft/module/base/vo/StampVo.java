package com.xjrsoft.module.base.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StampVo {

    @TableId("F_StampId")
    @JsonProperty("F_StampId")
    private String stampId;

    @JsonProperty("F_StampName")
    private String stampName;

    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("F_StampType")
    private String stampType;

    @JsonProperty("F_Password")
    private String password;

    @JsonProperty("F_File")
    private String imgFile;

    @JsonProperty("F_Sort")
    private String sort;

    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    @JsonProperty("F_File_Type")
    private Byte fileType;

    @JsonProperty("F_CreateUserId")
    private String createUserId;

    @JsonProperty("F_CreateUserName")
    private String createUserName;

    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    @JsonProperty("F_StampAttributes")
    private Integer stampAttributes;

    @JsonProperty("F_AuthorizeUser")
    private String authorizeUser;

    private static final long serialVersionUID = 1L;

}
