package com.xjrsoft.common.handler;

import com.xjrsoft.common.utils.DataAuthUtil;
import com.xjrsoft.core.launch.constant.TokenConstant;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import com.xjrsoft.module.base.entity.XjrBaseDataAuthorize;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataAuthHandler {

    // 数据配置类型（0-查看所有数据，1-仅查看本公司，2-仅查看本公司及所有下属公司，3-仅查看本部门，4-仅查看本部门及所有下属部门，5-仅查看本人，6-仅查看本人及所有下属员工）
    private static final int[] DATA_AUTH_LEVEL_ARRAY = {0, 2, 1, 4, 3, 6, 5};

    private static final String[] specialCompanyAuthUrls = {"/users", "/posts", "/departments", "/companies"};

    public ExpressionList getExpressionList() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }

        String moduleId = request.getHeader(TokenConstant.KEY_OF_HEADER_MODULE_ID);
        List<XjrBaseDataAuthorize> dataAuthorizeList = DataAuthUtil.getDataAuthListOfCurrentUser(moduleId);
        if (CollectionUtil.isEmpty(dataAuthorizeList)) {
            return null;
        }
        Integer resultTypeLevel = -1;
        for (XjrBaseDataAuthorize dataAuth : dataAuthorizeList) {
            Integer dataSettingType = dataAuth.getDataSettingType();
            if (dataSettingType == null) {
                continue;
            }
            int authLevel = this.getAuthLevel(dataSettingType);
            resultTypeLevel = resultTypeLevel < 0 || authLevel < resultTypeLevel ? authLevel : resultTypeLevel;
        }
        Integer resultType = DATA_AUTH_LEVEL_ARRAY[resultTypeLevel];
        String userId = SecureUtil.getUserId();
        // 组织架构，公司、部门、岗位、人员的特殊权限
        if (isOrganizedUrl(request.getRequestURI())) {
            return buildOrganizedExpressionList(userId, resultType);
        }

        // 普通模块权限
        List<Expression> expressionList = null;
        switch (resultType) {
            //查看所有数据
            case 0:
                break;
            case 1:
                //仅查看本公司
                expressionList = DataAuthUtil.getExpressionListOfUser(userId, OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, false);
                break;
            case 2:
                //仅查看本公司及所有下属公司
                expressionList = DataAuthUtil.getExpressionListOfUser(userId, OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, true);
                break;
            case 3:
                //仅查看本部门
                expressionList = DataAuthUtil.getExpressionListOfUser(userId, OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, false);
                break;
            case 4:
                //仅查看本部门及所有下属部门
                expressionList = DataAuthUtil.getExpressionListOfUser(userId, OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, true);
                break;
            case 5:
                //仅查看本人
                expressionList = new ArrayList<>();
                expressionList.add(new StringValue(userId));
                break;
            case 6:
                //仅查看本人及所有下属员工
                expressionList = DataAuthUtil.getExpressionListOfUser(userId, OrganizationCacheUtil.POST_LIST_CACHE_KEY, true);
                break;
        }
        return new ExpressionList(expressionList);
    }

    private ExpressionList buildOrganizedExpressionList(String userId, Integer resultType) {
        List<Expression> expressionList = null;
        switch (resultType) {
            //查看所有数据
            case 0:
                break;
            case 1:
                //仅查看本公司
                expressionList = DataAuthUtil.getExpressionListOfCompany(userId, false);
                break;
            case 2:
                //仅查看本公司及所有下属公司
                expressionList = DataAuthUtil.getExpressionListOfCompany(userId, true);
                break;
        }
        return new ExpressionList(expressionList);
    }

    private int getAuthLevel(Integer dataSettingType) {
        for (int i = 0; i < DATA_AUTH_LEVEL_ARRAY.length; i++) {
            if (DATA_AUTH_LEVEL_ARRAY[i] == dataSettingType.intValue()) {
                return i;
            }
        }
        return -1;
    }

    public String getDataAuthColumn() {
        if (isOrganizedUrl(WebUtil.getRequest().getRequestURI())) {
            return "F_CompanyId";
        }
        return "F_CreateUserId";
    }

    public boolean ignoreTable(String tableName) {
        String[] tables = {"xjr_base_databaselink", "xjr_base_annexesfile", "xjr_base_area", "xjr_base_datasource", "xjr_form_relation",
                "xjr_form_scheme", "xjr_form_schemeinfo", "xjr_base_authorize", "xjr_base_coderule", "xjr_base_coderuleseed", "xjr_base_dataauthorize",
                "xjr_base_dataitem", "xjr_base_dataitemdetail", "xjr_base_module", "xjr_base_modulebutton", "xjr_base_modulecolumn",
                "xjr_base_moduleform", "xjr_base_userrelation"};
        return Arrays.stream(tables).anyMatch(table -> StringUtil.equalsIgnoreCase(tableName, table));
    }

    public boolean isOrganizedUrl(String uri) {
        for (String prefix : specialCompanyAuthUrls) {
            if (StringUtil.startsWithIgnoreCase(uri, prefix)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOrgModule(String moduleId) {
        return false;
    }
}
