package com.xjrsoft.module.base.service;

import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBaseModuleButton;
import com.xjrsoft.module.base.entity.XjrBaseModuleColumn;
import com.xjrsoft.module.base.entity.XjrBaseModuleForm;
import com.xjrsoft.module.base.vo.MenuVo;
import com.xjrsoft.module.base.vo.ModuleOtherVo;
import com.xjrsoft.module.buildCode.dto.CodeSchemaDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-27
 */
public interface IXjrBaseModuleService extends IService<XjrBaseModule> {

    List<ModuleOtherVo> getSubAuthorizations();

    boolean saveModule(String moduleId, CodeSchemaDto codeSchemaDto);

    List<XjrBaseModuleButton> getButtonsByModuleId(String moduleId);

    List<XjrBaseModuleColumn> getColumnsByModuleId(String moduleId);

    List<XjrBaseModuleForm> getFormsByModuleId(String moduleId);

    boolean addModule(XjrBaseModule module, List<XjrBaseModuleButton> buttonList, List<XjrBaseModuleColumn> columnList, List<XjrBaseModuleForm> formList) throws Exception;

    boolean updateModule(String moduleId, XjrBaseModule module, List<XjrBaseModuleButton> buttonList, List<XjrBaseModuleColumn> columnList, List<XjrBaseModuleForm> formList) throws Exception;

    List<XjrBaseModule> getModulesByParentId(String moduleId);

    /**
     * 获取当前登录用户的授权菜单
     * @return
     */
    List<XjrBaseModule> getAuthModuleForCurrentUser(String systemId);

    /**
     * 获取用户/角色授权的菜单
     * @param objectId 用户/角色id
     * @param objectType 1-角色，2-用户
     * @return
     */
    List<XjrBaseModule> getAuthModuleForObject(String objectId, Integer objectType, String systemId);
    
}
