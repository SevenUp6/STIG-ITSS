package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppBaseInfoDto {

    @JsonProperty("btSelectItem")
    private Integer selectItem;

    @JsonProperty("F_Status")
    private Integer status;

    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    @JsonProperty("F_ModifyDate")
    private LocalDateTime modifyDate;

    @JsonProperty("$index")
    private Integer sortCode;

}
