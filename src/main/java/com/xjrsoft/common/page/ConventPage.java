package com.xjrsoft.common.page;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.istack.internal.NotNull;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringUtil;

import java.util.List;

public class ConventPage {
    public ConventPage() {
    }

    // 分页入参 转换为 IPage
    public static <T> IPage<T> getPage(@NotNull PageInput input) {
        Page<T> page = new Page((long) Convert.toInt(input.getLimit()), (long)Convert.toInt(input.getSize()));
        if(StringUtil.isNoneBlank(input.getOrder())){
            if ("asc".equals(input.getOrder())) {
                page.addOrder(OrderItem.asc(input.getOrderfield()));
            } else {
                page.addOrder(OrderItem.desc(input.getOrderfield()));
            }
        }

        return page;
    }

    // MyBatisPlus 分页返回  转换为 PageOutput格式
    public static <T> PageOutput<T> getPageOutput(IPage<T> page) {
        PageOutput<T> output = new PageOutput<>();
        output.setTotal(Convert.toInt(page.getTotal()));
        output.setRows(page.getRecords());
        return output;
    }

    // 2020年11月10日 10:41:00 最新版
    public static <T> PageOutput<T> getPageOutput(IPage<?> page, Class<T> clazz) {
        PageOutput<T> output = new PageOutput<>();
        output.setTotal(Convert.toInt(page.getTotal()));
        List<T> ts = BeanUtil.copyList(page.getRecords(), clazz);
        output.setRows(ts);
        return output;
    }

    public static <T> PageOutput<T> getPageOutput(Long total, List<T> records){
        PageOutput<T> output = new PageOutput<>();
        output.setTotal(Convert.toInt(total));
        output.setRows(records);
        return output;
    }
}
