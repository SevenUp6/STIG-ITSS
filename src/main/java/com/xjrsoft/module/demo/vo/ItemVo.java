package com.xjrsoft.module.demo.vo;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class ItemVo {
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
    private List<MaterialVo> materialVoList;

    @JsonProperty("F_OrderId")
    private String orderId;
    
    @JsonProperty("F_Count_Price")
    private String countPrice;
    
    @JsonProperty("showParams")
    private boolean showParams = false;
    
    public String getCountPrice() {
    	if (this.price == null || this.count == null) {
    		return StringPool.ZERO;
    	}
    	return (new BigDecimal(this.price).multiply(new BigDecimal(this.count)))
    			.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }
}
