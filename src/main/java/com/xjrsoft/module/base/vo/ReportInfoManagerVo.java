package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportInfoManagerVo implements INode {

    private static final long serialVersionUID = 1L;

    /**
     * 明细主键
     */
    @JsonIgnore
    private String itemDetailId;

    /**
     * 分类主键
     */
    @JsonIgnore
    private String itemId;

    /**
     * 父级主键
     */
    @JsonIgnore
    private String parentId;

    /**
     * 编码
     */
    @JsonIgnore
    private String itemCode;

    /**
     * 名称
     */
    @JsonProperty("F_Name")
    private String itemName;

    /**
     * 值
     */
    @JsonIgnore
    private String itemValue;

    /**
     * 快速查询
     */
    @JsonIgnore
    private String quickQuery;

    /**
     * 简拼
     */
    @JsonIgnore
    private String simpleSpelling;

    /**
     * 是否默认
     */
    @JsonIgnore
    private Integer isDefault;

    /**
     * 排序码
     */
    @JsonIgnore
    private Integer sortCode;

    /**
     * 有效标志
     */
    @JsonIgnore
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonIgnore
    private String description;

    @JsonProperty("children")
    private List<INode> children;

    @Override
    public String getId() {
        return itemDetailId;
    }

    @Override
    public List<INode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }
}
