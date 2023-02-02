package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.mp.base.BaseService;
import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;
import com.xjrsoft.module.base.mapper.XjrBaseDataAuthorizeMapper;
import com.xjrsoft.module.base.service.IXjrBaseDataAuthorizeService;
import com.xjrsoft.module.base.utils.AuthCacheUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 数据授权表 服务实现类
 *
 * @author job
 * @since 2021-01-27
 */
@Service
@AllArgsConstructor
public class XjrBaseDataAuthorizeServiceImpl extends BaseService<XjrBaseDataAuthorizeMapper, XjrBaseDataAuthorize> implements IXjrBaseDataAuthorizeService {

    @Override
    public List<XjrBaseDataAuthorize> getAuthorizedDataListForObject(String objectId, Integer objectType) {
        return this.list(Wrappers.<XjrBaseDataAuthorize>query().lambda()
                .eq(XjrBaseDataAuthorize::getObjectType, objectType)
                .eq(XjrBaseDataAuthorize::getObjectId, objectId).eq(XjrBaseDataAuthorize::getEnabledMark, 1));
    }

    public List<XjrBaseDataAuthorize> getDataAuthListForUser(String userId) {
        Wrapper<XjrBaseDataAuthorize> wrapper = Wrappers.<XjrBaseDataAuthorize>query().lambda()
                .eq(XjrBaseDataAuthorize::getObjectId, userId)
                .eq(XjrBaseDataAuthorize::getObjectType, 2)
                .eq(XjrBaseDataAuthorize::getEnabledMark, 1);
        return this.list(wrapper);
    }

    @Override
    public boolean authorizeData(XjrBaseDataAuthorize dataAuthorize, Set<String> moduleIds) {
        if (CollectionUtil.isEmpty(moduleIds)) {
            return false;
        }
        List<XjrBaseDataAuthorize> savedEntities = new ArrayList<>();
        List<XjrBaseDataAuthorize> oldList = this.list(Wrappers.<XjrBaseDataAuthorize>query().lambda()
                .eq(XjrBaseDataAuthorize::getObjectId, dataAuthorize.getObjectId())
                .eq(XjrBaseDataAuthorize::getObjectType, dataAuthorize.getObjectType())
                .in(XjrBaseDataAuthorize::getModuleId, moduleIds));
        List<String> removedIdList = new ArrayList<>();
        for (String moduleId : moduleIds) {
            boolean isNew = true;
            for (XjrBaseDataAuthorize oldDataAuth : oldList) {
                if (StringUtil.equals(oldDataAuth.getModuleId(), moduleId)) {
                    removedIdList.add(oldDataAuth.getId());
                    oldDataAuth.setModuleId(moduleId);
                    oldDataAuth.setDataSettingType(dataAuthorize.getDataSettingType());
                    Integer dataSettingType = dataAuthorize.getDataSettingType();
                    if (dataSettingType != null) {
                        oldDataAuth.setDataSettingType(dataSettingType);
                    }
                    Integer enabledMark = dataAuthorize.getEnabledMark();
                    if (enabledMark != null) {
                        oldDataAuth.setEnabledMark(enabledMark);
                    }
                    savedEntities.add(oldDataAuth);
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                XjrBaseDataAuthorize newDataAuthorize = BeanUtil.copy(dataAuthorize, XjrBaseDataAuthorize.class);
                newDataAuthorize.setId(IdWorker.get32UUID());
                newDataAuthorize.setModuleId(moduleId);
                savedEntities.add(newDataAuthorize);
            }
        }
        boolean isSuccess = (CollectionUtil.isEmpty(removedIdList) ? true : this.removeByIds(removedIdList)) &&
                (CollectionUtil.isEmpty(savedEntities) ? true : this.saveBatch(savedEntities));
        // 更新缓存
        AuthCacheUtil.remove(AuthCacheUtil.DATA_AUTH_CACHE_KEY, removedIdList);
        AuthCacheUtil.addCacheList(AuthCacheUtil.DATA_AUTH_CACHE_KEY, savedEntities);
        return isSuccess;
    }

    public List<XjrBaseDataAuthorize> getDataAuthListOfCurUserByUrl(String url) {
        XjrUser user = SecureUtil.getUser();
        String roleIdsStr = user.getRoleId();
        List<String> roleIds = StringUtil.isEmpty(roleIdsStr) ? null : Arrays.asList(StringUtils.split(roleIdsStr, StringPool.COMMA));
        return this.baseMapper.getDataAuthOfUserByUrl(url, user.getUserId(), roleIds);
    }
}
