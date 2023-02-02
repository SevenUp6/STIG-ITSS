package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseTempfield;
//import com.xjrsoft.module.workflow.dto.GetPageListNwfTaskDto;

import java.util.List;

/**
* @Author:湘北智造-框架开发组
* @Date:2020/10/26
* @Description:临时变量表
*/
public interface IXjrBaseTempfieldService extends IService<XjrBaseTempfield> {


    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/10/23
    * @Param:[idValue, type]
    * @return:com.xjrsoft.module.base.entity.XjrBaseTempfield
    * @Description:根据key和type查询数据
    */
    XjrBaseTempfield getByIdAndType(String idValue, String type);

     List<XjrBaseTempfield> getListByIdAndType(List<String> idValues, String type);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/10/23
    * @Param:[subProcess_img, subProcess_formData, subProcess_name]
    * @return:boolean
    * @Description:保存子流程信息
    */
    boolean saveSubProcessInfo(XjrBaseTempfield subProcess_img, XjrBaseTempfield subProcess_formData, XjrBaseTempfield subProcess_name);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/10/23
    * @Param:[idValue]
    * @return:List<XjrBaseTempfield>
    * @Description:根据key值找出子流程信息
    */
    List<XjrBaseTempfield> getSubProcessInfo(String idValue);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/10/23
    * @Param:[nwfSchemeInfoId, deploymentId]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseTempfield>
    * @Description:根据key中是否存在流程模板信息id和部署id去查找记录
    */
    List<XjrBaseTempfield> selectByNwfInfoIdOrDeplId(String nwfSchemeInfoId, String deploymentId);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/3
    * @Param:[type]
    * @return:java.util.List<XjrTempKeyvalue>
    * @Description:根据key值找出子流程信息
    */
//    IPage<XjrBaseTempfield> getByType(GetPageListNwfTaskDto dto, String type);

}
