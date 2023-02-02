package com.xjrsoft.module.base.mapper;

import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.entity.XjrBaseModuleButton;
import com.xjrsoft.module.base.entity.XjrBaseModuleColumn;
import com.xjrsoft.module.base.entity.XjrBaseModuleForm;
import com.xjrsoft.module.base.vo.ModuleTreeEntityVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-10-27
 */
public interface XjrBaseModuleMapper extends BaseMapper<XjrBaseModule> {

    List<String> getModuleIdsOfAuth(boolean isAdminUser, Integer objectType, String...objectId);

    List<XjrBaseModuleButton> getModuleBtnOfAuth(boolean isAdminUser, List<String> moduleIds, Integer objectType, String...objectIds);

    List<XjrBaseModuleColumn> getModuleColOfAuth(boolean isAdminUser, List<String> moduleIds, Integer objectType, String...objectIds);

    List<XjrBaseModuleForm> getModuleFormOfAuth(boolean isAdminUser, List<String> moduleIds, Integer objectType, String...objectIds);

    List<XjrBaseModule> getModuleOfAuth(String userId, List<String> roleIds, String systemId);

}
