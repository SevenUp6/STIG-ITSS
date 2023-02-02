package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetStampPageListDto;
import com.xjrsoft.module.base.dto.SaveStampDto;
import com.xjrsoft.module.base.entity.XjrBaseStamp;

/**
* @Author:光华科技-软件研发部
* @Date:2020/10/26
* @Description:工作流签章服务类
*/
public interface IXjrBaseStampService extends IService<XjrBaseStamp> {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/5
    * @Param:[dto, StampType:分类, EnabledMark:启用状态, StampAttributes:0私人签章 1 默认签章 2 公共签章]
    * @return:com.xjrsoft.common.page.PageOutput
    * @Description:获取签章列表(分页)
    */
    PageOutput getStampPageList(GetStampPageListDto dto, String userId);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/12/24
    * @Param:[id, userId]
    * @return:boolean
    * @Description:设置默认签章
    */
    boolean setDefaultStamp(String id, String userId);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/12/24
    * @Param:[id]
    * @return:boolean
    * @Description:检验签章密码是否正确
    */
    boolean validatePwd(String id, String password);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/12/24
    * @Param:[dto]
    * @return:boolean
    * @Description:保存签章
    */
    boolean saveStamp(SaveStampDto dto);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/12/24
    * @Param:[xjrBaseStamp]
    * @return:boolean
    * @Description:更新签章
    */
    boolean updateStamp(String id, SaveStampDto xjrBaseStamp);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2021/3/16
    * @Param:[id, AuthorizeUsers]
    * @return:boolean
    * @Description:添加授权用户
    */
    boolean addAuthorizeUser(String id, String AuthorizeUsers);

     Boolean checkMaintainUser(String id);

    boolean addMember(String id, String member);

    boolean addMaintain(String id, String maintain);
}
