package com.xjrsoft.module.base.dto;

import com.xjrsoft.common.page.PageInput;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 子系统表视图实体类
 *
 * @author Job
 * @since 2021-06-08
 */
@Data
    @ApiModel(value = "列表XjrBaseSubsystemDto对象", description = "子系统表")
public class XjrBaseSubsystemListDto extends PageInput {

    //@JsonProperty("F_Name")
    private String F_Name;


}
