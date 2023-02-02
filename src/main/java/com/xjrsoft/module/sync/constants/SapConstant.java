package com.xjrsoft.module.sync.constants;

public interface SapConstant {

    String SAP_COMPANY_FUNCTION = "ZHR_GET_ORGEH";
    String SAP_COMPANY_TABLENAME = "T_ORGEH";

    String SAP_POST_FUNCTION = "ZHR_GET_PLANS";
    String SAP_POST_TABLENAME = "T_PLANS";

    String SAP_USER_FUNCTION = "ZHR_GET_PERNR";
    String SAP_USER_TABLENAME = "T_PERNR";

    // 转换对应字段
    String[] SAP_COMPANY_FIELDS = {"OBJID", "STEXT"};
    String[] XJR_COMPANY_FIELDS = {"enCode", "fullName"};

    String[] SAP_POST_FIELDS = {"OBJID", "STEXT"};
    String[] XJR_POST_FIELDS = {"enCode", "name"};

    String[] SAP_USER_FIELDS = {"NACHN", "GESCH", "GBDAT", "USRID_LONG", "USRID", "ZDHFJHM"};
    String[] XJR_USER_FIELDS = {"realName", "gender", "birthday", "email", "mobile", "telephone"};

}
