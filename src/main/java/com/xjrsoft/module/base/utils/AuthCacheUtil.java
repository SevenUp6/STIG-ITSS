package com.xjrsoft.module.base.utils;

import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;
import com.xjrsoft.module.base.service.IXjrBaseDataAuthorizeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class AuthCacheUtil {

    public static final String DATA_AUTH_CACHE_KEY = "sys_data_authorized";

    private static final RedisTemplate redisTemplate;

    private static final IXjrBaseDataAuthorizeService dataAuthService;

    static {
        redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
        dataAuthService = SpringUtil.getBean(IXjrBaseDataAuthorizeService.class);
    }

    private AuthCacheUtil(){}

    public static List getCachesList(String key) {
        List list = null;
        if (!redisTemplate.hasKey(key)) {
            list = loadDataAuthList();
        } else {
            list = redisTemplate.opsForList().range(key, 0, -1);
        }
        return list;
    }
    
    public static void remove(String cacheKey, List<String> idList) {
        List<XjrBaseDataAuthorize> list = getCachesList(cacheKey);
        List<XjrBaseDataAuthorize> removedList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            for (XjrBaseDataAuthorize dataAuth : list) {
                if (idList.contains(dataAuth.getId())) {
                    redisTemplate.opsForList().remove(cacheKey, 0, dataAuth);
                }
            }
        }
    }

    public static List<XjrBaseDataAuthorize> loadDataAuthList() {
        List list = list = dataAuthService.list();
        if (CollectionUtil.isNotEmpty(list)) {
            redisTemplate.opsForList().rightPushAll(DATA_AUTH_CACHE_KEY, list);
        }
        return list;
    }

    public static boolean addCacheList(String cacheKey, List list) {
        return redisTemplate.opsForList().rightPushAll(cacheKey, list) > 0L;
    }
}
