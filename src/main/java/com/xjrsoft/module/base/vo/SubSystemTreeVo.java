package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubSystemTreeVo {

    // 复制F_Id
    @JsonProperty("F_ModuleId")
    private String moduleId;

    // 复制F_Id
    @JsonProperty("F_SubSystemId")
    private String subSystemId;

    @JsonProperty("F_Id")
    private String id;

    @JsonProperty("F_Name")
    private String name;

    @JsonProperty("F_EnCode")
    private String enCode;

    @JsonProperty("F_SortCode")
    private Integer sortCode;

    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("children")
    private List<ModuleVo> moduleVoList;

    public List<ModuleVo> getModuleVoList() {
        if (this.moduleVoList == null) {
            this.moduleVoList = new ArrayList<>();
        }
        return this.moduleVoList;
    }

    public void setId(String id){
        this.moduleId = id;
        this.subSystemId = id;
        this.id = id;
    }
}
