package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CodeRuleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编码规则主键
     */
    @JsonProperty("F_RuleId")
    private String ruleId;

    /**
     * 编号
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 名称
     */
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * 当前流水号
     */
    @JsonProperty("F_CurrentNumber")
    private String currentNumber;

    /**
     * 规则格式Json
     */
    @JsonProperty("F_RuleFormatJson")
    private String ruleFormatJson;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建用户
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;
}
