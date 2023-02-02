package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorizeIdsDto {

    @JsonProperty("moduleJson")
    private List<String> moduleIdList;

    @JsonProperty("buttonJson")
    private List<String> buttonIdList;

    @JsonProperty("columnJson")
    private List<String> columnIdList;

    @JsonProperty("formJson")
    private List<String> formIdList;

    @JsonProperty("subSystemJson")
    private List<String> subSystemIdList;

    public List<String> getModuleIdList() {
        if (moduleIdList == null) {
            moduleIdList = new ArrayList<>();
        }
        return moduleIdList;
    }

    public List<String> getButtonIdList() {
        if (buttonIdList == null) {
            buttonIdList = new ArrayList<>();
        }
        return buttonIdList;
    }

    public List<String> getColumnIdList() {
        if (columnIdList == null) {
            columnIdList = new ArrayList<>();
        }
        return columnIdList;
    }

    public List<String> getFormIdList() {
        if (formIdList == null) {
            formIdList = new ArrayList<>();
        }
        return formIdList;
    }
}
