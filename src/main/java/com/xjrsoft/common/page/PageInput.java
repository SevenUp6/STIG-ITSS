package com.xjrsoft.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@ApiModel(value = "PageInput", description = "分页入参")
public class PageInput implements Serializable {

    public PageInput(){
    }
    
    @ApiModelProperty(value = "当前页标")
    private int limit = 1;
    @ApiModelProperty(value = "每页大小")
    private int size = 15;
    @ApiModelProperty(value = "关键词")
    private String keyword;
    @ApiModelProperty(value = "排序字段")
    private String orderfield;
    @ApiModelProperty(value = "排序方式 asc  desc")
    private String order;
}
