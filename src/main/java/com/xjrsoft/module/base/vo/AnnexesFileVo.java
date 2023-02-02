package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnnexesFileVo {

    @JsonProperty("F_Id")
    private String id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("F_FileName")
    private String fileName;
}
