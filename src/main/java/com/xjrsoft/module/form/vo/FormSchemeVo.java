package com.xjrsoft.module.form.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormSchemeVo {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 对应信息主键
     */
    @JsonProperty("F_SchemeInfoId")
    private String schemeInfoId;

    /**
     * 1.正式2.草稿
     */
    @JsonProperty("F_Type")
    private Integer type;

    /**
     * 模板内容
     */
    @JsonProperty("F_Scheme")
    private String scheme;

    /**
     * 创建日期
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建人主键
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建人名称
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;
}
