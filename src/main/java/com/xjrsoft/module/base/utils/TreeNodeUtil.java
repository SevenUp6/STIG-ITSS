package com.xjrsoft.module.base.utils;

import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.node.INode;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class TreeNodeUtil {
    private TreeNodeUtil(){}


    public static void findChildrenId(INode node, List<String> childIdList) {
        List<INode> children = node.getChildren();
        if (CollectionUtil.isNotEmpty(children)) {
            for (INode child : children) {
                findChildrenId(child, childIdList);
            }
        } else {
            childIdList.add(node.getId());
        }
    }

    public static List<INode> getNodeListOfTree(INode entity) {
        List<INode> resultList = new ArrayList<>();
        List<INode> children = entity.getChildren();
        if (CollectionUtil.isNotEmpty(children)) {
            for (INode node : children) {
                resultList.addAll(getNodeListOfTree(node));
            }
        }
        resultList.add(entity);
        return resultList;
    }

    public static List<INode> getNodeAndChildrenList(String objectId, String cacheKey) {
        List<? extends INode> cacheList = OrganizationCacheUtil.getNodeListCaches(cacheKey);
        ForestNodeMerger.merge(cacheList);
        INode targetNode = null;
        for (INode node : cacheList) {
            if (StringUtil.equals(objectId, node.getId())) {
                targetNode = node;
                break;
            }
        }
        return getNodeListOfTree(targetNode);
    }
}
