package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SchemeDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

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
}
