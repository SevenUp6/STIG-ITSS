package com.xjrsoft.core.tool.node;

import com.xjrsoft.core.tool.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ForestNodeMerger {
    public ForestNodeMerger() {
    }

    public static <T extends INode> List<T> merge(List<T> items) {
        ForestNodeManager<T> forestNodeManager = new ForestNodeManager(items);
        items.forEach((forestNode) -> {
            String parentId = forestNode.getParentId();
            if (!StringUtil.equals(parentId, "0") || !StringUtil.isEmpty(parentId)) {
                INode node = forestNodeManager.getTreeNodeAT(forestNode.getParentId());
                if (node != null) {
                    node.getChildren().add(forestNode);
                } else {
                    forestNodeManager.addParentId(forestNode.getId());
                }
            }

        });
        return forestNodeManager.getRoot();
    }

    public static List<Map<String,Object>> merge(List<Map<String,Object>> maps,String field,String parentfield) {
        //遍历所有
        maps.forEach((item) ->{
            List<Map<String,Object>> childMap = new ArrayList<>();
            //每条数据插入子节点属性
            item.put("children",childMap);
            //所有非一级节点
            if(!(item.get(parentfield).toString().equals("0")) || !StringUtil.isEmpty(item.get(parentfield))){
                //找到当前节点的父级
                maps.forEach((it) ->{
                    if(it.get(field).toString().equals(item.get(parentfield).toString())){
                        List<Map<String,Object>> children = (List<Map<String,Object>>)it.get("children");
                        if(children == null){
                            children= new ArrayList<>();
                            it.put("children",children);
                        }
                        children.add(item);
                    }
                });
            }
        });
        //获取到所有一级节点  此时 所有 子节点已经根据parentfield  已经插入children
        List<Map<String, Object>> result = maps.stream().filter(x -> x.get(parentfield).equals("0") || StringUtil.isEmpty(x.get(parentfield))).collect(Collectors.toList());

        return result;
    }
}