package com.xjrsoft.module.language.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LgTypeDto {


    /**
     * 主键ID
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 语言名称
     */
    @TableField("F_Name")
    @JsonProperty("F_Name")
    private String name;

    /**
     * 语言编码（不予许重复）
     */
    @JsonProperty("F_Code")
    @TableField("F_Code")
    private String code;

    /**
     * 是否是主语言0不是1是
     */
    @JsonProperty("F_IsMain")
    @TableField("F_IsMain")
    private Integer isMain;
}
