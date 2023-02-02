package com.xjrsoft.module.form.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FormSchemeDetailVo {

    @JsonProperty("Scheme")
    private FormSchemeVo formSchemeVo;

    @JsonProperty("SchemeInfo")
    private FormSchemeInfoVo formSchemeInfoVo;
}
