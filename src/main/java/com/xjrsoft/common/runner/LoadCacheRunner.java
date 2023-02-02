package com.xjrsoft.common.runner;

import com.xjrsoft.module.base.utils.AuthCacheUtil;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadCacheRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 加载公司
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY);
        // 加载部门
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
        // 加载用户对应关系
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY);
        // 加载岗位
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.POST_LIST_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.POST_LIST_CACHE_KEY);
        // 加载角色
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.ROLE_LIST_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.ROLE_LIST_CACHE_KEY);
        // 加载用户
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.USER_LIST_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.USER_LIST_CACHE_KEY);
        // 加载数据权限
        OrganizationCacheUtil.clearCaches(AuthCacheUtil.DATA_AUTH_CACHE_KEY);
        AuthCacheUtil.loadDataAuthList();

        // 加载数据源
        OrganizationCacheUtil.clearCaches(OrganizationCacheUtil.DATASOURCE_CACHE_KEY);
        OrganizationCacheUtil.loadListCaches(OrganizationCacheUtil.DATASOURCE_CACHE_KEY);
    }
}
