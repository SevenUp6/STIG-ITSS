package com.xjrsoft.common.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.xjrsoft.common.dbmodel.DbExecutor;
import com.xjrsoft.common.dbmodel.utils.DataSourceUtil;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.entity.XjrBaseUserRelation;
import com.xjrsoft.module.base.utils.AuthCacheUtil;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class DataAuthUtil {

    private DataAuthUtil(){}

    /**
     * 获取菜单的数据权限列表
     * @param moduleId 菜单id
     * @return
     */
    public static List<XjrBaseDataAuthorize> getDataAuthListOfCurrentUser(String moduleId) {
        if (StringUtil.isEmpty(moduleId)) {
            return null;
        }
        List<XjrBaseDataAuthorize> resultList = new ArrayList<>();
        List<XjrBaseDataAuthorize> dataAuthList = AuthCacheUtil.getCachesList(AuthCacheUtil.DATA_AUTH_CACHE_KEY);
        String currentUserId = SecureUtil.getUserId();
        List<String> roleIdList = OrganizationCacheUtil.getRoleIdsOfUser(currentUserId);
        if (CollectionUtil.isNotEmpty(dataAuthList)) {
            for (XjrBaseDataAuthorize dataAuth : dataAuthList) {
                if (dataAuth.getEnabledMark() == 1 && StringUtil.equals(dataAuth.getModuleId(), moduleId) &&
                        ((StringUtil.equals(dataAuth.getObjectId(), currentUserId) && dataAuth.getObjectType() == 2) ||
                                (roleIdList.contains(dataAuth.getObjectId()) && dataAuth.getObjectType() == 1))) {
                    resultList.add(dataAuth);
                }
            }
        }
        return resultList;
    }

    public static List<Expression> getExpressionListOfUser(String userId, String cacheKey, boolean isLoadSub) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new StringValue(userId));

        List<String> nodeIdList = OrganizationCacheUtil.getOrganizedIdsByUserId(userId, cacheKey, isLoadSub);
//        OrganizationCacheUtil.getOrganizedIdsByUserId()
//        List<String> nodeIdList = nodeList.stream().map(node -> node.getId()).collect(Collectors.toList());
        if (StringUtil.equals(cacheKey, OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY) ||
                StringUtil.equals(cacheKey, OrganizationCacheUtil.POST_LIST_CACHE_KEY)) {
            int category = OrganizationCacheUtil.getCategoryByCacheKey(cacheKey);
            List<XjrBaseUserRelation> userRelationList = getUserRelationBy(nodeIdList, category);
            expressionList.addAll(userRelationList.stream().map(userRelation -> new StringValue(userRelation.getUserId())).collect(Collectors.toList()));
        } else if (StringUtil.equals(cacheKey, OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY)) {
            List<XjrBaseUser> userList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.USER_LIST_CACHE_KEY);
            if (CollectionUtil.isNotEmpty(userList) && CollectionUtil.isNotEmpty(nodeIdList)) {
                for (XjrBaseUser user : userList) {
                    if (nodeIdList.contains(user.getCompanyId())) {
                        expressionList.add(new StringValue(user.getUserId()));
                    }
                }
            }
        }
        return expressionList;
    }

    public static List<Expression> getExpressionListOfCompany(String userId, boolean isLoadSub) {
        List<Expression> expressionList = new ArrayList<>();
        if (isLoadSub) {
            List<String> nodeIdList = OrganizationCacheUtil.getOrganizedIdsByUserId(userId, OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, true);
            for (String companyId : nodeIdList) {
                expressionList.add(new StringValue(companyId));
            }
        } else {
            XjrBaseUser user = OrganizationCacheUtil.getCacheUserById(userId);
            expressionList.add(new StringValue(user.getCompanyId()));
        }
        return expressionList;
    }

    public static List<XjrBaseUserRelation> getUserRelationBy(List<String> objectIdList, Integer category) {
        List<XjrBaseUserRelation> userRelationList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY);
        List<XjrBaseUserRelation> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRelationList)) {
            for (XjrBaseUserRelation userRelation: userRelationList) {
                if (objectIdList.contains(userRelation.getObjectId()) && category == userRelation.getCategory()) {
                    resultList.add(userRelation);
                }
            }
        }
        return resultList;
    }

    public static boolean isContainsField(String tableName, String fieldName) {
        List<TableInfo> tableInfoList = TableInfoHelper.getTableInfos();
        for (TableInfo tableInfo : tableInfoList) {
            if (StringUtil.equalsIgnoreCase(tableInfo.getTableName(), tableName)) {
                List<TableFieldInfo> fieldList = tableInfo.getFieldList();
                for (TableFieldInfo field : fieldList) {
                    if (StringUtil.equalsIgnoreCase(fieldName, field.getColumn())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
