package com.xjrsoft.module.base.utils;

import com.xjrsoft.module.base.vo.PageInfoVo;
import com.xjrsoft.module.base.vo.SynchronizationVo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  ListUtils {
    public static PageInfoVo Pager(int pageSize, int pageIndex, List list) {
        //使用list 中的sublist方法分页
        List dataList = new ArrayList();
        PageInfoVo pageInfoVo = new PageInfoVo();
        // 每页显示多少条记录
        int currentPage; //当前第几页数据
        int totalRecord = list.size(); // 一共多少条记录
        int totalPage = totalRecord % pageSize; // 一共多少页
        if (totalPage > 0) {
            totalPage = totalRecord / pageSize + 1;
        } else {
            totalPage = totalRecord / pageSize;
        }
        pageInfoVo.setTotal(totalRecord);
// 当前第几页数据
        currentPage = totalPage < pageIndex ? totalPage : pageIndex;
// 起始索引
        int fromIndex = pageSize * (currentPage - 1);
// 结束索引
        int toIndex = pageSize * currentPage > totalRecord ? totalRecord : pageSize * currentPage;
        try {
            if (list.size() > 0) {
                dataList = list.subList(fromIndex, toIndex);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        pageInfoVo.setRows(dataList);
        return pageInfoVo;
    }

    //执行模糊查询
    public static List<SynchronizationVo> seach(List<SynchronizationVo> list, String keyword) {
        List results = new ArrayList();
        Pattern pattern = Pattern.compile(keyword);
        for (int n = 0; n < list.size(); n++) {
            Matcher matcher = pattern.matcher(list.get(n).getName());
            if (matcher.find()) {
                results.add(list.get(n));
            }
        }
        return results;
    }
}
