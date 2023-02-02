package com.xjrsoft.module.form.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.module.form.vo.ModuleFormVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormRelationDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 表单主键
     */
    @JsonProperty("F_FormId")
    private String formId;

    /**
     * 功能主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

    /**
     * 发布的设置信息
     */
    @JsonProperty("F_SettingJson")
    private String settingJson;
}
