package com.xjrsoft.module.demo.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaterialVo {
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

    @JsonProperty("F_OrderId")
    private String orderId;

    @JsonProperty("F_ItemId")
    private String itemId;

    @JsonProperty("F_Discount_Price")
    private String discountPrice;
    
    public String getDiscountPrice() {
    	if (this.price == null || this.count == null || this.discount == null) {
    		return StringPool.ZERO;
    	}
    	return (new BigDecimal(this.price).multiply(new BigDecimal(this.count))
    			.multiply(new BigDecimal(this.discount))).setScale(2, RoundingMode.HALF_UP)
    			.stripTrailingZeros().toPlainString();
    }
}
