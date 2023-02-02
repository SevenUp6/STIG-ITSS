package com.xjrsoft.common.core;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VoToColumn {

    //获取对象所有列名
    public static  List<String> Convert(@NotNull Class entity){
        Field[] fields = entity.getDeclaredFields();
        String[] columns = new String[fields.length];

        for (int i = 1; i < fields.length; i++) {
            JsonProperty annotation = fields[i].getAnnotation(JsonProperty.class);
            if(null == annotation){ continue; }
            columns[i -1] = annotation.value();
        }
        columns[fields.length - 1] = "createUserId";
        return  Arrays.asList(columns) ;

    }

    //获取对象所有列名
    public static  Predicate<TableFieldInfo> Convert(@NotNull Class from,Class to){

        Field[] fromFields = from.getDeclaredFields();
        List<String> collect = Arrays.asList(fromFields).stream().map(x -> x.getName()).collect(Collectors.toList());
        Field[] toFields = to.getDeclaredFields();

        Predicate<TableFieldInfo> cod = x -> true;
        List<String> list = new ArrayList<>();

        //遍历所有vo属性
        for (Field toField : toFields) {
            //如果vo的属性 在实体类又相同的
            if(collect.contains(toField.getName())){
                cod.and(x -> x.getColumn().equals(toField.getName()));
            }
//            cod.and(x -> x.getColumn().equals(collect.get(0).getName()));
//            list.add(collect.get(0).getName());
        }
        System.out.println(list);
        return cod;
    }
}
