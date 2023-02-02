package com.xjrsoft.module.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaterialDto {

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 物料名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 折扣
     */
    @JsonProperty("F_Price")
    private Double price;

    /**
     * 数量
     */
    @JsonProperty("F_Count")
    private Integer count;

    /**
     * 折扣
     */
    @JsonProperty("F_Discount")
    private Double discount;

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
}
