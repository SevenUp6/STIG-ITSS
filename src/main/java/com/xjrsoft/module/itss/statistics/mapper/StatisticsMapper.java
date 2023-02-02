package com.xjrsoft.module.itss.statistics.mapper;

import java.util.List;

/**
 * 设备模块表 Mapper 接口
 *
 * @author HANHE
 * @since 2022-10-12
 */
public interface StatisticsMapper  {

    List getSbgzData(String type_id, String start,String end);
    List getGzlxData(String type_id, String mod_id,String start,String end);

    List getClsxData(String type_id, String start,String end);

    List getGdslData();

    List getGdclData_0(String type_id, String start,String end);

    List getGdmxData(String type_id, String start,String end);
}
