package com.xjrsoft.core.tool.node;

import java.util.List;

public interface INode {
    String getId();

    String getParentId();

    List<INode> getChildren();
}