package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetStampPageListDto extends GetPageListDto {

    /**
     * 部门主键
     */
    @JsonProperty("StampType")
    private String stampType;

    /**
     * 是否有效标识
     */
    @JsonProperty("EnabledMark")
    private String enabledMark;

    /**
     *
     */
    @JsonProperty("StampAttributes")
    private String stampAttributes;


}