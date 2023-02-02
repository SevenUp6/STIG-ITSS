package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.core.tool.node.INode;
import com.xjrsoft.core.tool.utils.Func;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class SqlTreeDataVo extends HashMap implements INode {

    @JsonIgnore
    private String idName;

    @JsonIgnore
    private String parentIdName;

    @Override
    public String getId() {
        return Func.toStr(this.get(idName));
    }

    @Override
    public String getParentId() {
        return Func.toStr(this.get(parentIdName));
    }

    @Override
    public List<INode> getChildren() {
        Object obj = this.get("children");
        List<INode> children = null;
        if (obj == null) {
            children = new ArrayList<>();
            this.put("children", children);
        } else {
            children = (List<INode>) obj;
        }
        return children;
    }
}
