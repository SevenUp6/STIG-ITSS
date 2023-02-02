package com.xjrsoft.module.excel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.excel.entity.XjrExcelImportfileds;

import java.util.List;

/**
* @Author:湘北智造-框架开发组
* @Date:2020/11/11
* @Description:Excel导入字段转换关系表 服务类
*/
public interface IXjrExcelImportfiledsService extends IService<XjrExcelImportfileds> {

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/11
    * @Param:[id]
    * @return:java.util.List<com.xjrsoft.module.excel.entity.XjrExcelImportfileds>
    * @Description:根据importId获取导入字段
    */
    List<XjrExcelImportfileds> listByImportId(String id);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/11
    * @Param:[id]
    * @return:com.xjrsoft.module.excel.entity.XjrExcelImportfileds
    * @Description:根据importId获取主键
    */
    XjrExcelImportfileds getPmKey(String id);

    List<XjrExcelImportfileds> getByImportId(String importId);
}
