package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataItemDto {
    private static final long serialVersionUID = 1L;

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
     * 分类编码
     */
    @JsonProperty("F_ItemCode")
    private String itemCode;

    /**
     * 分类名称
     */
    @JsonProperty("F_ItemName")
    private String itemName;

    /**
     * 树型结构
     */
    @JsonProperty("F_IsTree")
    private Integer isTree;

    /**
     * 导航标记
     */
    @JsonProperty("F_IsNav")
    private Integer isNav;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
