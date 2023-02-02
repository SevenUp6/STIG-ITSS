package com.xjrsoft.module.language.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 多语言语言类型表
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_lg_type")
public class XjrLgType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("F_Id")
    // @JsonProperty("F_Id")
    private String id;

    /**
     * 语言名称
     */
    @TableField("F_Name")
    // @JsonProperty("F_Name")
    private String name;

    /**
     * 语言编码（不予许重复）
     */
    @TableField("F_Code")
    // @JsonProperty("F_Code")
    private String code;

    /**
     * 是否是主语言0不是1是
     */
    @TableField("F_IsMain")
    // @JsonProperty("F_IsMain")
    private Integer isMain;
}
