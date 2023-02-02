package com.xjrsoft.module.form.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.vo.ModuleTreeEntityVo;
import lombok.Data;

@Data
public class SystemFormVo {

    private String formId;

    private String moduleId;

    private String parentId;

    private String url;

    private String name;

    private String enCode;

    private String component;

    @JsonIgnore
    private ModuleTreeEntityVo parentModule;

    public String getUrl() {
        StringBuilder urlSb = new StringBuilder();
        if(StringUtil.isNotBlank(url)) {
             urlSb = new StringBuilder(this.url);
            if (parentModule != null) {
                parentModule.buildFullUrlAddress(urlSb);
            }
        }
        return urlSb.toString();
    }
}
