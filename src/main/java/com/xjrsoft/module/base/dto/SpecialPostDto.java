package com.xjrsoft.module.base.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpecialPostDto {
    private static final long serialVersionUID = 1L;

//    @TableId("F_Id")
//    private String id;

    @JsonProperty("F_Name")
    @JSONField(name = "F_Name")
    private String name;

    @JsonProperty("F_Remark")
    @JSONField(name = "F_Remark")
    private String remark;

    @JsonProperty("F_Sort")
    @JSONField(name = "F_Sort")
    private Integer sort;

    @JsonProperty("F_Type")
    @JSONField(name = "F_Type")
    private Integer type;

    @JsonProperty("F_Temp1")
    @JSONField(name = "F_Temp1")
    private String temp1;

    @JsonProperty("F_Temp2")
    @JSONField(name = "F_Temp2")
    private String temp2;
}
