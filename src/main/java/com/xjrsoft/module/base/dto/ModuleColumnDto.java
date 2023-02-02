package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleColumnDto {
    private static final long serialVersionUID = 1L;

    /**
     * 列主键
     */
    @JsonProperty("F_ModuleColumnId")
    private String moduleColumnId;

    /**
     * 功能主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

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
}
