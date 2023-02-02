package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.entity.XjrBaseUserRelation;
import com.xjrsoft.module.base.mapper.XjrBaseUserRelationMapper;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.SpecialPostUserVo;
import com.xjrsoft.module.base.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-04
 */
@Service
public class XjrBaseUserRelationServiceImpl extends ServiceImpl<XjrBaseUserRelationMapper, XjrBaseUserRelation> implements IXjrBaseUserRelationService {

    @Override
    public boolean saveSpecialPostRelations(Map<String, List<String>> postUserJson, String objectId, Integer objectType) {
        List<XjrBaseUserRelation> savedList = new ArrayList<>();
        for (Map.Entry<String, List<String>> objEntry : postUserJson.entrySet()) {
            String specialPostId = objEntry.getKey();
            for (Object obj : objEntry.getValue()) {
                String userId = String.valueOf(obj);
                XjrBaseUserRelation userRelation = new XjrBaseUserRelation();
                userRelation.setCategory(objectType);
                userRelation.setObjectId(specialPostId + StringPool.UNDERSCORE + objectId);
                userRelation.setUserId(userId);
                savedList.add(userRelation);
            }
        }
        return this.saveBatch(savedList, 10);
    }

    @Override
    public List<SpecialPostUserVo> getUsersOfSpecialPosts(String objectId, Integer objectType) {
        if (!StringUtil.isEmpty(objectId)) {
            objectId = StringPool.PERCENT + StringPool.UNDERSCORE + objectId;
        }
        return baseMapper.getUsersOfSpecialPosts(objectId, objectType);
    }

    public boolean removeSpecialPostRelations(String objectId, Integer objectType) {
        objectId = StringPool.PERCENT + StringPool.UNDERSCORE + objectId;
        return this.remove(Wrappers.<XjrBaseUserRelation>query().lambda().like(XjrBaseUserRelation::getObjectId, objectId)
                .eq(XjrBaseUserRelation::getCategory, objectType));
    }

    @Override
    public boolean addUserRelationsForObject(String objectId, Integer objectType, List<String> userIdList) {
        List<XjrBaseUserRelation> userRelationList = this.list(Wrappers.<XjrBaseUserRelation>query().lambda().eq(XjrBaseUserRelation::getObjectId, objectId).eq(XjrBaseUserRelation::getCategory, objectType));
        if (CollectionUtil.isNotEmpty(userRelationList)) {
            // 删除已存在的数据
            List<String> userRelationIdList = userRelationList.stream().map(XjrBaseUserRelation::getUserRelationId).collect(Collectors.toList());
            this.removeByIds(userRelationIdList);
            OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY, userRelationIdList);
        }
        if (CollectionUtil.isNotEmpty(userIdList)) {
            List<XjrBaseUserRelation> addedList = new ArrayList<>(userIdList.size());
            userIdList.forEach(userId ->{
                XjrBaseUserRelation userRelation = new XjrBaseUserRelation();
                userRelation.setUserRelationId(StringUtil.randomUUID());
                userRelation.setUserId(userId);
                userRelation.setObjectId(objectId);
                userRelation.setCategory(objectType);
                userRelation.setCreateDate(LocalDateTime.now());
                userRelation.setCreateUserId(SecureUtil.getUserId());
                userRelation.setCreateUserName(SecureUtil.getUserName());
                addedList.add(userRelation);
            });
            return this.saveBatch(addedList) && OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY, addedList);
        }
        return true;
    }

    public boolean addUserRelationsForUser(String userId, Integer objectType, List<String> objectIdList, boolean isRemovingExits) {
        if (isRemovingExits) {
            List<XjrBaseUserRelation> userRelationList = this.list(Wrappers.<XjrBaseUserRelation>query().lambda().eq(XjrBaseUserRelation::getUserId, userId).eq(XjrBaseUserRelation::getCategory, objectType));
            if (CollectionUtil.isNotEmpty(userRelationList)) {
                // 删除已存在的数据
                List<String> userRelationIdList = userRelationList.stream().map(XjrBaseUserRelation::getUserRelationId).collect(Collectors.toList());
                this.removeByIds(userRelationIdList);
                OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY, userRelationIdList);
            }
        }
        if (CollectionUtil.isNotEmpty(objectIdList)) {
            List<XjrBaseUserRelation> addedList = new ArrayList<>(objectIdList.size());
            objectIdList.forEach(objectId ->{
                XjrBaseUserRelation userRelation = new XjrBaseUserRelation();
                userRelation.setUserId(userId);
                userRelation.setObjectId(objectId);
                userRelation.setCategory(objectType);
                addedList.add(userRelation);
            });
            return this.saveBatch(addedList) && OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY, addedList);
        }
        return true;
    }

    @Override
    public List<XjrBaseUserRelation> getObjectsOfUser(String userId, Integer objectType) {
        QueryWrapper<XjrBaseUserRelation> query = new QueryWrapper<>();
        query.lambda().eq(XjrBaseUserRelation::getCategory, objectType).eq(StringUtils.isNotBlank(userId), XjrBaseUserRelation::getUserId, userId);
        return baseMapper.selectList(query);
    }

    @Override
    public List<XjrBaseUser> getUsersOfObject(String objectId, Integer objectType) {
        return baseMapper.getUsersOfObject(objectId, objectType);
    }

    @Override
    public List<UserVo> getMemberUserVoListOfObject(String objectId, Integer objectType, String keyword) {
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        return baseMapper.getMemberUserVoListOfObject(objectId, objectType, keyword);
    }
}
