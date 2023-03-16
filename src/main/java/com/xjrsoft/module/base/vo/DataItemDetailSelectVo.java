package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataItemDetailSelectVo {

    private static final long serialVersionUID = 1L;

    /**
     * 明细主键
     */
    @JsonProperty("F_ItemDetailId")
    private String itemDetailId;

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


}
