package com.xjrsoft.common.utils;

import org.apache.commons.lang3.StringUtils;

public class DbUtil {


    private DbUtil(){}

    public static String getDriverByType(String dbType){
        String lowerType = StringUtils.lowerCase(dbType);
        switch (lowerType){
            case "mysql":
                return "com.mysql.cj.jdbc.Driver";
            case "oracle":
                return "oracle.jdbc.driver.OracleDriver";
            case  "dameng":
                return "dm.jdbc.driver.DmDriver";
            default:
                return null;
        }

    }
}
