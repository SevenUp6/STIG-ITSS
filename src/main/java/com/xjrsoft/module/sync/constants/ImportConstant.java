package com.xjrsoft.module.sync.constants;

public interface ImportConstant {

    String[] IMPORT_COMPANY_FIELDS = {"enCode", "fullName", "shortName", "parentId", "manager", "description"};

    String[] IMPORT_DEPARTMENT_FIELDS = {"enCode", "fullName", "shortName", "parentId", "companyId", "manager", "description"};

    String[] IMPORT_POST_FIELDS = {"enCode", "name", "parentId", "departmentId", "description"};

    String[] IMPORT_ROLE_FIELDS = {"enCode", "fullName", "", "description"};

    String[] IMPORT_USER_FIELDS = {"account", "enCode", "realName", "gender", "birthday", "mobile", "telephone", "email", "weChat", "departmentId", "description"};
}
