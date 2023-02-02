package com.xjrsoft.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.entity.XjrBaseRole;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
public interface XjrBaseRoleMapper extends BaseMapper<XjrBaseRole> {

    List<String> getRoleIdsForUser(String userId);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/6
    * @Param:[userId]
    * @return:java.util.List<Xjr_base_role>
    * @Description:查询用户的角色
    */
    List<XjrBaseRole> getRolesByUserId(String userId);

    List<XjrBaseRole> getAppRolesByUserId(String userId);
}
