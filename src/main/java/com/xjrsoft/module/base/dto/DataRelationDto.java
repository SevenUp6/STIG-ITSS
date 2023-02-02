package com.xjrsoft.module.base.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class DataRelationDto {

    /**
     * 名称
     */
    @JsonProperty("F_Name")
    private String Name;

    /**
     * 1.普通权限 2.自定义表单权限
     */
    @JsonProperty("F_Type")
    private Integer Type;

    /**
     * 对应模块主键
     */
    @JsonProperty("F_InterfaceId")
    private String InterfaceId;

    /**
     * 对象主键
     */
    @JsonProperty("F_ObjectId")
    private String ObjectId;

    /**
     * 对象类型1.角色2.用户
     */
    @JsonProperty("F_ObjectType")
    private Integer ObjectType;

    /**
     * 条件公式
     */
    @JsonProperty("F_Formula")
    private String Formula;
}
