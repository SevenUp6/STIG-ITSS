package com.xjrsoft.module.language.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LgTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 语言名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 语言编码（不予许重复）
     */
    @JsonProperty("F_Code")
    private String code;

    /**
     * 是否是主语言0不是1是
     */
    @JsonProperty("F_IsMain")
    private Integer isMain;
}
