package com.xjrsoft.module.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 商品名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 摆放位置
     */
    @JsonProperty("F_Deposition")
    private String deposition;

    /**
     * 更换周期（周/次）
     */
    @JsonProperty("F_Replacement")
    private Integer replacement;

    /**
     * 最低高度
     */
    @JsonProperty("F_MinHeight")
    private Double minHeight;

    /**
     * 最高高度
     */
    @JsonProperty("F_MaxHeight")
    private Double maxHeight;

    /**
     * 单价
     */
    @JsonProperty("F_Price")
    private Double price;

    /**
     * 是否成品（0-是，1-否）
     */
    @JsonProperty("F_IsProduct")
    private Integer isProduct;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("F_Count")
    private Integer count;

    @JsonProperty("params")
    private List<MaterialDto> materialDtoList;
}
