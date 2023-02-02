package com.xjrsoft.module.base.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class AreaVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 区域主键
     */
    @JsonProperty("F_AreaId")
    private String areaId;
    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 区域编码
     */
    @JsonProperty("F_AreaCode")
    private String areaCode;

    /**
     * 区域名称
     */
    @JsonProperty("F_AreaName")
    private String areaName;

    /**
     * 快速查询
     */
    @JsonProperty("F_QuickQuery")
    private String quickQuery;

    /**
     * 简拼
     */
    @JsonProperty("F_SimpleSpelling")
    private String simpleSpelling;

    /**
     * 层次
     */
    @JsonProperty("F_Layer")
    private Integer layer;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;
}
