package com.xjrsoft.module.base.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataAuthorizedVo {
    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String id;
    /**
     * 菜单主键
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;
    /**
     * 授权对象主键
     */
    @JsonProperty("F_ObjectId")
    private String objectId;
    /**
     * 授权对象类型(1-角色，2-用户)
     */
    @JsonProperty("F_ObjectType")
    private Integer objectType;
    /**
     * 启用配置(0-不启用，1-启用)
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;
    /**
     * 子级功能沿用父级配置(0-不启用，1-启用)
     */
    @JsonProperty("F_EnabledChildrenMark")
    private Integer enabledChildrenMark;
    /**
     * 数据配置类型（0-查看所有数据，1-仅查看本公司，2-仅查看本公司及所有下属公司，3-仅查看本部门，4-仅查看本部门及所有下属部门，5-仅查看本人，6-仅查看本人及所有下属员工）
     */
    @JsonProperty("F_DataSettingType")
    private Integer dataSettingType;
}
