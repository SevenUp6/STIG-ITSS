package com.xjrsoft.module.base.utils;

import com.xjrsoft.common.allenum.OperateTypeEnum;

import javax.servlet.http.HttpServletRequest;

public final class LogUtil {
    private LogUtil(){}

    /**
     * @title 获取操作类型
     * @create 2020年11月11日 19:23:30
     * */
    public static OperateTypeEnum getOperateType(HttpServletRequest request){
        if(request.getRequestURI().equals("/login")){
            return OperateTypeEnum.LOGIN;
        }else if(request.getMethod().toLowerCase().equals("get")){
            return OperateTypeEnum.VISIT;
        }else if(request.getMethod().toLowerCase().equals("post")){
            return OperateTypeEnum.CREATE;
        }else if(request.getMethod().toLowerCase().equals("put")){
            return OperateTypeEnum.UPDATE;
        }else if (request.getMethod().toLowerCase().equals("patch")){
            return OperateTypeEnum.CREATE;
        }else if (request.getMethod().toLowerCase().equals("delete")){
            return OperateTypeEnum.DELETE;
        }else {
            return OperateTypeEnum.OTHER;
        }
    }

    /**
     * @title 获取浏览器
     * @create 2020年11月11日 19:23:30
     * */
    public static String getBrowser(String str){
        String browerStr = "未知";
        if(str.toLowerCase().contains("chrome")){
            browerStr = "Chrome";
        }
        else if(str.toLowerCase().contains("firefox")){
            browerStr = "Firefox";
        }
        else{
        }

        return  browerStr;
    }
}
