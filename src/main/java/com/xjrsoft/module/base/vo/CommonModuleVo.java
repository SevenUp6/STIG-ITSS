package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommonModuleVo {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 用户主键
     */
    @JsonProperty("F_UserId")
    private String userId;

    /**
     * 系统功能主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

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
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;
}
