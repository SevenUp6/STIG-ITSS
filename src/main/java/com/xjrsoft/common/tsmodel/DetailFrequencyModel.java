package com.xjrsoft.common.tsmodel;

import lombok.Data;

import java.util.List;


/**
 *@title 明细频率类
 */
@Data
public class DetailFrequencyModel {

    /**
     *小时
     */
    public String hour;

    /**
     *分钟
     */
    public String minute;

    /**
     *间隔类型 每天day，每周week，每月month
     */
    public String type;

    /**
     *间隔执行值
     */
    public String carryDate;

    /**
     *执行月
     */
    public List<String> carryMounth;
}
