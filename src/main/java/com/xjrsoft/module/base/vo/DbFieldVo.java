package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DbFieldVo {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 字段名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 数据类型
     */
    @JsonProperty("F_DataType")
    private String dataType;

    /**
     * 说明
     */
    @JsonProperty("F_Remark")
    private String remark;

    /**
     * 创建日期
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建人ID
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建人
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    @JsonProperty("F_Length")
    private Integer length;
}
