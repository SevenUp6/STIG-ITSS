package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleFormDto {
    private static final long serialVersionUID = 1L;

    /**
     * 列主键
     */
    @JsonProperty("F_ModuleFormId")
    private String moduleFormId;

    /**
     * 功能主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

    /**
     * 编码
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 名称
     */
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    @JsonProperty("F_IsRequired")
    private Integer isRequired;
}
