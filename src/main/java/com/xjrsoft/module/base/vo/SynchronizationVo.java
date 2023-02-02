package com.xjrsoft.module.base.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *同步信息返回
 */

@Data
@Accessors(chain = true)
public class SynchronizationVo {

    @JsonProperty("F_Name")
    private String name;
    
    @JsonProperty("F_Status")
    private Integer status;

  
    @JsonProperty("F_Info")
    private String info;
    
}
