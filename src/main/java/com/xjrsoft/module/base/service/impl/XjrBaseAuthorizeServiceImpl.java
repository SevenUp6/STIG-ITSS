package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.AuthorizeIdsDto;
import com.xjrsoft.module.base.entity.XjrBaseAuthorize;
import com.xjrsoft.module.base.mapper.XjrBaseAuthorizeMapper;
import com.xjrsoft.module.base.service.IXjrBaseAuthorizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 授权功能表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Service
public class XjrBaseAuthorizeServiceImpl extends ServiceImpl<XjrBaseAuthorizeMapper, XjrBaseAuthorize> implements IXjrBaseAuthorizeService {

    @Override
    public boolean submit(String objectId, Integer objectType, AuthorizeIdsDto authorizeIdsDto) {
        this.remove(Wrappers.<XjrBaseAuthorize>query().lambda().eq(XjrBaseAuthorize::getObjectId, objectId).eq(XjrBaseAuthorize::getObjectType, objectType));

        List<XjrBaseAuthorize> addedList = new ArrayList<>();
        List<String> moduleIds = authorizeIdsDto.getModuleIdList();
        buildAuthorize(objectId, objectType, 1, moduleIds, addedList);


        List<String> buttonIds = authorizeIdsDto.getButtonIdList();
        buildAuthorize(objectId, objectType, 2, buttonIds, addedList);


        List<String> columnIds = authorizeIdsDto.getColumnIdList();
        buildAuthorize(objectId, objectType, 3, columnIds, addedList);


        List<String> formIds = authorizeIdsDto.getFormIdList();
        buildAuthorize(objectId, objectType, 4, formIds, addedList);

        // 子系统
        List<String> subSystemIds = authorizeIdsDto.getSubSystemIdList();
        buildAuthorize(objectId, objectType, 9, subSystemIds, addedList);

        if (CollectionUtil.isNotEmpty(addedList)) {
            return this.saveBatch(addedList);
        }
        return true;
    }

    private void buildAuthorize(String objectId, Integer objectType, Integer itemType, List<String> itemIds, List<XjrBaseAuthorize> authorizeList){
        itemIds.forEach(itemId -> {
            XjrBaseAuthorize authorize = new XjrBaseAuthorize();
            authorize.setAuthorizeId(StringUtil.randomUUID());
            authorize.setObjectId(objectId);
            authorize.setObjectType(objectType);
            authorize.setItemId(itemId);
            authorize.setItemType(itemType);
            authorizeList.add(authorize);
        });
    }
}
