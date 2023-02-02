package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DbDraftVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("F_Id")
    private String id;

    /**
     * 表名
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 内容
     */
    @JsonProperty("F_Content")
    private String content;

    /**
     * 表备注
     */
    @JsonProperty("F_Remark")
    private String remark;
}
