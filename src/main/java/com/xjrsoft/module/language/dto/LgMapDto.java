package com.xjrsoft.module.language.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "LgMapDto", description = "语言映照表分页入参")
public class LgMapDto {
    /**
     * 主键ID
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 显示内容
     */
   @JsonProperty("F_Name")
    private String name;

    /**
     * 编码(系统自动产生，作为关联项)
     */
   @JsonProperty("F_Code")
    private String code;

    /**
     * 对应语言显示编码
     */
   @JsonProperty("F_TypeCode")
    private String typeCode;
}
