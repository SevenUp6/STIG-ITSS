package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataItemDetailDto {
    private static final long serialVersionUID = 1L;

    /**
     * 明细主键
     */
    @JsonProperty("F_ItemDetailId")
    private String itemDetailId;

    /**
     * 分类主键
     */
    @JsonProperty("F_ItemId")
    private String itemId;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 编码
     */
    @JsonProperty("F_ItemCode")
    private String itemCode;

    /**
     * 名称
     */
    @JsonProperty("F_ItemName")
    private String itemName;

    /**
     * 值
     */
    @JsonProperty("F_ItemValue")
    private String itemValue;

    /**
     * 简拼
     */
    @JsonProperty("F_SimpleSpelling")
    private String simpleSpelling;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 有效标志
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
