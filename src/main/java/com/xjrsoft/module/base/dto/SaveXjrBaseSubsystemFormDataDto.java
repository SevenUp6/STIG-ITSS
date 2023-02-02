package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存子系统表数据传输对象实体类
 *
 * @author Job
 * @since 2021-06-08
 */
@Data
public class SaveXjrBaseSubsystemFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("xjr_base_subsystemEntity")
    private XjrBaseSubsystemDto subsystemDto;


}
