package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class CodeRuleDto {
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
    private Integer ortCode;

    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
