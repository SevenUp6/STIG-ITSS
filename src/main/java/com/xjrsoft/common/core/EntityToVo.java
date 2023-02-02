package com.xjrsoft.common.core;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityToVo{
    public EntityToVo() {
    }


    public static  <T> IPage<T> pageVO(IPage pages,Class<T> res) throws Exception {
        List<T> list = new ArrayList<T>();
        for (Object item : pages.getRecords()) {

            T ts = res.newInstance();
            BeanUtil.copyProperties(item,ts);
            list.add(ts);
        }
        IPage<T> pageVo = new Page(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(list);
        return pageVo;
    }
}