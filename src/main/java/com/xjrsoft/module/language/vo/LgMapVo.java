package com.xjrsoft.module.language.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 语言映照表
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Data
public class LgMapVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 显示内容
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 编码(系统自动产生，作为关联项)
     */
    @JsonProperty("F_Code")
    private String code;

    /**
     * 对应语言显示编码
     */
    @JsonProperty("F_TypeCode")
    private String typeCode;
}
