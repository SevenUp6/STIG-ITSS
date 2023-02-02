package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;

@Data
public class ModuleOtherVo {

    @JsonProperty("moduleid")
    private String moduleId;

    @JsonProperty("button")
    private Set<String> buttons;

    @JsonProperty("column")
    private Set<Map<String, String>> columns;

    @JsonProperty("form")
    private Set<String> forms;

    public Set<String> getButtons(){
        if (this.buttons == null) {
            this.buttons = new LinkedHashSet<>();
        }
        return this.buttons;
    }

    public Set<Map<String, String>> getColumns(){
        if (this.columns == null) {
            this.columns = new LinkedHashSet<>();
        }
        return this.columns;
    }

    public Set<String> getForms(){
        if (this.forms == null) {
            this.forms = new LinkedHashSet<>();
        }
        return this.forms;
    }
}
