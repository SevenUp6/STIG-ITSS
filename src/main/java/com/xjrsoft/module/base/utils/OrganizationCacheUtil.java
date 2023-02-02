package com.xjrsoft.module.base.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.cache.CacheAble;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.node.INode;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.vo.CompanyTreeVo;
import com.xjrsoft.module.base.vo.DepartmentTreeVo;
import com.xjrsoft.module.base.vo.PostTreeVo;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

public final class OrganizationCacheUtil {

    private OrganizationCacheUtil(){}

    private static final RedisTemplate redisTemplate;

    private  static final IXjrBaseCompanyService companyService;

    private  static final IXjrBaseDepartmentService departmentService;

    private  static final IXjrBasePostService postService;

    private  static final IXjrBaseRoleService roleService;

    private  static final IXjrBaseUserRelationService userRelationService;

    private  static final IXjrBaseDatasourceService datasourceService;

    private static final IXjrBaseUserService userService;

    public static final String COMPANY_LIST_CACHE_KEY = "sys_company_list";

    public static final String DEPARTMENT_LIST_CACHE_KEY = "sys_department_list";

    public static final String POST_LIST_CACHE_KEY = "sys_post_list";

    public static final String ROLE_LIST_CACHE_KEY = "sys_role_list";

    public static final String USER_RELATION_LIST_CACHE_KEY = "sys_user_relation_list";

    public static final String USER_LIST_CACHE_KEY = "sys_user_list";

    public static final String DATASOURCE_CACHE_KEY = "sys_datasource_list";

    static {
        redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
        companyService = SpringUtil.getBean(IXjrBaseCompanyService.class);
        departmentService = SpringUtil.getBean(IXjrBaseDepartmentService.class);
        postService = SpringUtil.getBean(IXjrBasePostService.class);
        roleService = SpringUtil.getBean(IXjrBaseRoleService.class);
        userRelationService = SpringUtil.getBean(IXjrBaseUserRelationService.class);
        userService = SpringUtil.getBean(IXjrBaseUserService.class);
        datasourceService = SpringUtil.getBean(IXjrBaseDatasourceService.class);
    }

    public static List getListCaches(String key) {
        List<CacheAble> list = null;
        if (!redisTemplate.hasKey(key)) {
            list = loadListCaches(key);
        } else {
            list = redisTemplate.opsForList().range(key, 0, -1);
        }
        return list;
    }

    public static List<? extends INode> getNodeListCaches(String cacheKey) {
        List list = getListCaches(cacheKey);
        Class<? extends INode> nodeClass = getNodeClassByCacheKey(cacheKey);
        return BeanUtil.copyList(list, nodeClass);
    }

    public static List loadListCaches(String key){
        IService service = getServiceByCacheKey(key);
        List list = service.list();
        if (CollectionUtil.isNotEmpty(list)) {
            redisTemplate.opsForList().rightPushAll(key, list);
        }
        return list;
    }

    public static boolean setListCaches(String key, Collection<?> collection) {
        return redisTemplate.opsForList().rightPushAll(key, collection) > 0L;
    }

    public static boolean add(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value) > 0L;
    }

    public static void remove(String key, String...ids) {
        ListOperations listOperations = redisTemplate.opsForList();
        List<Object> list = getListCaches(key);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        List<Object> removedList = new ArrayList<>();
        List<String> idList = Arrays.asList(ids);
        for (Object obj : list) {
            String objId = null;
            if (obj instanceof XjrBaseCompany) {
                objId = ((XjrBaseCompany) obj).getCompanyId();
                break;
            } else if (obj instanceof XjrBaseDepartment) {
                objId = ((XjrBaseDepartment) obj).getDepartmentId();
            }else if (obj instanceof XjrBaseDepartment) {
                objId = ((XjrBasePost) obj).getPostId();
            }else if (obj instanceof XjrBaseDepartment) {
                objId = ((XjrBaseRole) obj).getRoleId();
            } else if (obj instanceof XjrBaseUserRelation) {
                objId = ((XjrBaseUserRelation) obj).getUserRelationId();
            }
            if (objId != null && idList.contains(objId)) {
                removedList.add(obj);
            }
        }
        for (Object obj :removedList) {
            listOperations.remove(key, 0, obj);
        }
    }

    public static List<String> getOrganizedIdsByUserId(String userId, String cacheKey, boolean isLoadSub){
        List<String> resultList = new ArrayList<>();
        if (StringUtil.equals(cacheKey, COMPANY_LIST_CACHE_KEY)) {
            XjrBaseUser user = getCacheUserById(userId);
            resultList.add(user.getCompanyId());
        } else {
            List<XjrBaseUserRelation> userRelationList = getListCaches(USER_RELATION_LIST_CACHE_KEY);
            int category = getCategoryByCacheKey(cacheKey);
            for (XjrBaseUserRelation userRelation : userRelationList) {
                if (category == userRelation.getCategory() &&
                        StringUtil.equals(userId, userRelation.getUserId())) {
                    resultList.add(userRelation.getObjectId());
                }
            }
        }
        // 加载下级
        if (isLoadSub) {
            Set<String> subIdList = new LinkedHashSet<>();
            List<? extends INode> nodeList = getNodeListCaches(cacheKey);
            ForestNodeMerger.merge(nodeList);
            for (INode node : nodeList) {
                if (resultList.contains(node.getId())) {
                    subIdList.addAll(TreeNodeUtil.getNodeListOfTree(node).stream().map(subNode -> subNode.getId()).collect(Collectors.toList()));
                }
            }
            resultList.addAll(subIdList);
        }
        return resultList;
    }

    public static List<String> getRoleIdsOfUser(String userId){
        List<XjrBaseUserRelation> userRelationList = getListCaches(USER_RELATION_LIST_CACHE_KEY);
        List<String> resultList = new ArrayList<>();
        for (XjrBaseUserRelation userRelation : userRelationList) {
            if (StringUtil.equals(userId, userRelation.getUserId()) && userRelation.getCategory() == 1) {
                resultList.add(userRelation.getObjectId());
            }
        }
        return resultList;
    }

    private static IService getServiceByCacheKey(String cacheKey) {
        IService service = null;
        switch (cacheKey) {
            case COMPANY_LIST_CACHE_KEY:
                service = companyService;
                break;
            case DEPARTMENT_LIST_CACHE_KEY:
                service = departmentService;
                break;
            case POST_LIST_CACHE_KEY:
                service = postService;
                break;
            case ROLE_LIST_CACHE_KEY:
                service = roleService;
                break;
            case USER_RELATION_LIST_CACHE_KEY:
                service = userRelationService;
                break;
            case USER_LIST_CACHE_KEY:
                service = userService;
                break;
            case DATASOURCE_CACHE_KEY:
                service = datasourceService;
        }
        return service;
    }

    private static <T extends INode> Class<? extends INode> getNodeClassByCacheKey(String cacheKey) {
        Class<? extends INode> nodeClass = null;
        switch (cacheKey) {
            case COMPANY_LIST_CACHE_KEY:
                nodeClass = CompanyTreeVo.class;
                break;
            case DEPARTMENT_LIST_CACHE_KEY:
                nodeClass = DepartmentTreeVo.class;
                break;
            case POST_LIST_CACHE_KEY:
                nodeClass = PostTreeVo.class;
                break;
        }
        return nodeClass;
    }

    public static int getCategoryByCacheKey(String cacheKey) {
        int category = 0;
        switch (cacheKey) {
            case ROLE_LIST_CACHE_KEY:
                category = 1;
                break;
            case POST_LIST_CACHE_KEY:
                category = 2;
                break;
            case DEPARTMENT_LIST_CACHE_KEY:
                category = 3;
                break;
        }
        return category;
    }

    public static boolean clearCaches(String cacheKey) {
        return redisTemplate.delete(cacheKey);
    }

    public static XjrBaseUser getCacheUserById(String userId) {
        List<XjrBaseUser> userListCaches = getListCaches(USER_LIST_CACHE_KEY);
        for (XjrBaseUser user : userListCaches) {
            if (StringUtil.equals(userId, user.getUserId())) {
                return user;
            }
        }
        return null;
    }

    public static boolean addCache(String cacheKey, CacheAble o) {
        return redisTemplate.opsForList().rightPush(cacheKey, o) > 0;
    }

    public static boolean addCacheList(String cacheKey, List<? extends CacheAble> cacheList) {
        return redisTemplate.opsForList().rightPushAll(cacheKey, cacheList) > 0;
    }

    public static boolean updateCache(String cacheKey, CacheAble o) {
        List<CacheAble> listCaches = getListCaches(cacheKey);
        for (CacheAble cache : listCaches) {
            if (StringUtil.equals(cache.getCacheId(), o.getCacheId())) {
                redisTemplate.opsForList().remove(cacheKey, 0, cache);
                BeanUtil.copy(o, cache);
                return addCache(cacheKey, cache);
            }
        }
        return false;
    }

    public static boolean removeCaches(String cacheKey, String...ids) {
        return removeCaches(cacheKey, Arrays.asList(ids));
    }

    public static boolean removeCaches(String cacheKey, List<String> idList) {
        List<CacheAble> listCaches = getListCaches(cacheKey);
        for (CacheAble cache : listCaches) {
            for (String id : idList) {
                if (StringUtil.equals(cache.getCacheId(), id)) {
                    return redisTemplate.opsForList().remove(cacheKey, 0, cache) > 0;
                }
            }
        }
        return false;
    }

    public static <T extends CacheAble> T getCacheById(String cacheKey, String id) {
        List<T> listCaches = getListCaches(cacheKey);
        for (T cacheAble: listCaches) {
            if (StringUtil.equals(id, cacheAble.getCacheId())) {
                return cacheAble;
            }
        }
        return null;
    }

    public static <T extends CacheAble> List<T> getCacheListByIds(String cacheKey, Collection<Object> idList) {
        List<T> resultList = new ArrayList<>();
        List<T> listCaches = getListCaches(cacheKey);
        for (T cacheAble: listCaches) {
            if (idList.contains(cacheAble.getCacheId())) {
                resultList.add(cacheAble);
            }
        }
        return resultList;
    }
}
