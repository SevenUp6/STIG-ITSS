package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.node.INode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataItemTreeVo implements INode {

    private static final long serialVersionUID = 1L;
    /**
     * 分类主键
     */
    @JsonProperty("F_ItemId")
    private String itemId;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 分类编码
     */
    @JsonProperty("F_ItemCode")
    private String itemCode;

    /**
     * 分类名称
     */
    @JsonProperty("F_ItemName")
    private String itemName;

    /**
     * 树型结构
     */
    @JsonProperty("F_IsTree")
    private Integer isTree;

    /**
     * 导航标记
     */
    @JsonProperty("F_IsNav")
    private Integer isNav;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    private List<INode> children;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    @Override
    @JsonIgnore
    public String getId() {
        return this.itemId;
    }

    @Override
    public List<INode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }
}
