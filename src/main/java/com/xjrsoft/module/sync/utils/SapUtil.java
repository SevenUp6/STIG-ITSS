package com.xjrsoft.module.sync.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.xjrsoft.core.tool.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * SAP请求工具类
 */
@Slf4j
public final class SapUtil {

    public static final String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

    private SapUtil(){}

    public static JCoTable getTableList(String functionStr, String tableName, Map<String, Object> param) {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "172.17.116.123");//IP
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "02");//系统编号
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "800");//客户端编号
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, "OA_HCM");//用户名
        // 注：密码是区分大小写的，要注意大小写
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "1");//密码
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "1");//语言
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");

        File cfg = new File(ABAP_AS_POOLED + "." + "jcoDestination");
        System.err.println(cfg.getPath());
        if (!cfg.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                connectProperties.store(fos, "");
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JCoFunction function = null;
        JCoDestination destination = null;
        try {
            destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
            function = destination.getRepository().getFunction(functionStr);
            if (CollectionUtils.isNotEmpty(param)) {
                JCoParameterList input = function.getImportParameterList();
                // 遍历map中的键
                for (String key : param.keySet()) {
                    input.setValue(key, param.get(key));
                }
            }

            function.execute(destination);
            JCoTable tb = function.getTableParameterList().getTable(tableName);

            return tb;
        } catch (Exception e) {
            log.error("请求SAP接口报错：" + functionStr, e);
        }
        return null;
    }

    public static <T> List<T> toXjrEntities(String[] xjrFields, String[] sapFields, JCoTable jCoTable, Class<T> entityClass) {
        List<T> resultList = new ArrayList<>();
        int numRows = jCoTable.getNumRows();
        for (int i = 0; i < numRows; i++) {
            try {
                T entity = entityClass.newInstance();
                for (int j = 0; j < xjrFields.length; i++) {
                    jCoTable.setRow(j);
                    BeanUtil.setProperty(entity, xjrFields[j], jCoTable.getValue(sapFields[j]));
                }
                resultList.add(entity);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
