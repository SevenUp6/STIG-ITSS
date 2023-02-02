package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.vo.RoleVo;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
public interface IXjrBaseRoleService extends IService<XjrBaseRole> {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/6
    * @Param:[dto]
    * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取列表页
    */
    PageOutput<RoleVo> getRolePageList(GetPageListDto dto);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/6
    * @Param:[userId]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取用户的角色
    */
    List<XjrBaseRole> getRolesByUserId(String userId);

    List<XjrBaseRole> getAppRolesByUserId(String userId);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2021/1/13
    * @Param:[]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取超级管理员角色
    */
    XjrBaseRole getSysRole();

    boolean isAdminRole(String roleId);
}
