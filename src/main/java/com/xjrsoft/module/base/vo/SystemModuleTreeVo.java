package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SystemModuleTreeVo {

    @JsonProperty("mainSystemModuleList")
    private List<ModuleVo> moduleVoList;

    @JsonProperty("subSystemModuleList")
    private List<SubSystemTreeVo> subSystemTreeVoList;
}
