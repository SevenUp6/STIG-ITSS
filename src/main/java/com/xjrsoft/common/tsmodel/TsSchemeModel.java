package com.xjrsoft.common.tsmodel;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 *@title 任务信息详情
 */
@Data
public class TsSchemeModel {

    /**
     * 开始方式   1配置完立即执行   2根据设置的开始时间
     */
    private Integer startType;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束方法 1无限期  2有结束时间
     */
    private Integer endType;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 执行频率类别
     * 1：只执行一次
     * 2：简单重复执行 涉及分钟，小时，天，周
     * 3：明细频率设置
     * 4：表达式设置 corn表达式
     */
    private Integer executeType;


    /**
     *间隔时间值 对应2
     */
    private Integer simpleValue;

    /**
     * 间隔类型 对应2 minute分hours小时day天week周
     */
    private String simpleType;

    /**
     *间隔类型 对应3 频率明显
     */
    private List<DetailFrequencyModel> frequencyList;

    /**
     * cron表达式 对应4
     */
    private String cornValue;


    /**
     * 是否重启1是0不是
     */
    private Integer isRestart;

    /**
     * 间隔重启时间（分钟）
     */
    private Integer restartMinute;

    /**
     * 重启次数
     */
    private Integer restartNum;

    /**
     * 方法类型1sql 2存储过程 3接口 4ioc依赖注入
     */
    private Integer methodType;

    /**
     * 数据ID
     */
    private String dbId;

    /**
     * sql语句
     */
    private String strSql;


    /**
     * 存储过程
     */
    private String procName;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口请求方式 1get 2post
     */
    private Integer urlType;

    /**
     * 依赖注入方法名
     */
    private String iocName;
}
