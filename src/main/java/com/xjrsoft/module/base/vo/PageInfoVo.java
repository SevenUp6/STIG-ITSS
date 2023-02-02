package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageInfoVo {

    /**
     * 信息
     */
    @JsonProperty("Rows")
    private List rows;
    
    /**
     * 总记录数
     */
    @JsonProperty("Total")
    private Integer total;
}
