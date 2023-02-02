package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.page.PageInput;
import com.xjrsoft.common.page.PageOutput;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GetLogPageListDto", description = "获取日志分页列表入参")
public class GetLogPageListDto extends PageInput {

    /**
     * 日志分类
     */
    @ApiModelProperty(value = "日志分类")
    public int categoryId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    public LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    public LocalDateTime endTime;


}
