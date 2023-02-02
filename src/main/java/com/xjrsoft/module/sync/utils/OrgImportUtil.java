package com.xjrsoft.module.sync.utils;

import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
public final class OrgImportUtil {

    private OrgImportUtil(){}

    public static <T> List<T> parseExcelToEntities(File file, String[] fieldNames, Class<T> entityClass, Set<String> errorMsgList) {
        List<T> resultList = new ArrayList();
        Workbook workbook = null;
        try {
            if (StringUtil.endsWithIgnoreCase(file.getName(), ".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                // 第一个格子没有值，则认为是最后一行
                Cell first = row.getCell(0);
                if (first == null || first.getCellTypeEnum() == CellType.BLANK) {
                    break;
                }
                T entity = entityClass.newInstance();
                boolean isError = false;
                for (int i = 0; i < fieldNames.length; i++) {
                    String fieldName = fieldNames[i];
                    if (StringUtil.isEmpty(fieldName)) {
                        continue;
                    }
                    Class<?> propertyType = BeanUtil.getPropertyDescriptor(entityClass, fieldName).getPropertyType();
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        continue;
                    }
                    Object value = null;
                    try {
                        if (propertyType == String.class) {
                            value = cell.getStringCellValue();
                        } else if (propertyType == Integer.class) {
                            value = (int) cell.getNumericCellValue();
                        } else if (propertyType == LocalDateTime.class) {
                            Date date = cell.getDateCellValue();
                            value = date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                        }
                    } catch (IllegalStateException e) {
                        log.error("数据类型类型异常！" + e.getMessage() + "[" + rowNum + ", " + i +"]", e);
                        errorMsgList.add("数据类型类型异常！" + e.getMessage() + "，位置：[" + rowNum + ", " + i +"]");
                        isError = true;
                        break;
                    }
                    BeanUtil.setProperty(entity, fieldName, value);
                }
                if (!isError) {
                    resultList.add(entity);
                }
            }
        } catch (Exception e) {
            log.error("解析excel失败！", e);
            throw new RuntimeException("解析excel失败！" + e.getMessage() ,e);
        }
        return resultList;
    }

    public static List<XjrBaseDepartment> checkDuplicateDepartment(List<XjrBaseDepartment> departmentList, Set<String> errorMsgList) {
        List<XjrBaseDepartment> resultList = new ArrayList<>();
        List<XjrBaseDepartment> cacheList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
        if (CollectionUtil.isNotEmpty(departmentList) && CollectionUtil.isNotEmpty(cacheList)) {
            for (XjrBaseDepartment department : departmentList) {
                boolean isDuplicatedEnCode = false;
                boolean isDuplicatedName = false;
                String enCode = department.getEnCode();
                String name = department.getFullName();
                for (XjrBaseDepartment cache : cacheList) {
                    if (!StringUtil.isEmpty(enCode) && StringUtil.equals(enCode, cache.getEnCode())) {
                        isDuplicatedEnCode = true;
                    }
                    if (!StringUtil.isEmpty(name) && StringUtil.equals(name, cache.getFullName())) {
                        isDuplicatedName = true;
                    }
                }
                if(!isDuplicatedEnCode) {
                    for (XjrBaseDepartment checkedDepartment : departmentList) {
                        if (StringUtil.equals(department.getDepartmentId(), checkedDepartment.getDepartmentId())) {
                            continue;
                        }
                        if (StringUtil.equals(enCode, checkedDepartment.getEnCode())) {
                            isDuplicatedEnCode = true;
                        }
                        if (StringUtil.equals(name, checkedDepartment.getFullName())) {
                            isDuplicatedName = true;
                        }
                    }
                }
                if (isDuplicatedEnCode) {
                    errorMsgList.add("编码重复：" + enCode);
                }
                if (isDuplicatedName) {
                    errorMsgList.add("名称重复：" + name);
                }
                if (!isDuplicatedEnCode && !isDuplicatedName) {
                    resultList.add(department);
                }
            }
        }
        return resultList;
    }

    public static List<XjrBasePost> checkDuplicatePost(List<XjrBasePost> postList, Set<String> errorMsgList) {
        List<XjrBasePost> resultList = new ArrayList<>();
        List<XjrBasePost> cacheList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.POST_LIST_CACHE_KEY);
        if (CollectionUtil.isNotEmpty(postList) && CollectionUtil.isNotEmpty(cacheList)) {
            for (XjrBasePost post : postList) {
                boolean isDuplicatedEnCode = false;
                String enCode = post.getEnCode();
                for (XjrBasePost cache : cacheList) {
                    if (!StringUtil.isEmpty(enCode) && StringUtil.equals(enCode, cache.getEnCode())) {
                        isDuplicatedEnCode = true;
                        break;
                    }
                }
                if(!isDuplicatedEnCode) {
                    for (XjrBasePost checkedPost : postList) {
                        if (StringUtil.equals(post.getPostId(), checkedPost.getPostId())) {
                            continue;
                        }
                        if (StringUtil.equals(enCode, checkedPost.getEnCode())) {
                            isDuplicatedEnCode = true;
                            break;
                        }
                    }
                }
                if (isDuplicatedEnCode) {
                    errorMsgList.add("编码重复：" + enCode);
                } else {
                    resultList.add(post);
                }
            }
        }
        return resultList;
    }

    public static List<XjrBaseUser> checkDuplicateUser(List<XjrBaseUser> userList, Set<String> errorMsgList) {
        List<XjrBaseUser> resultList = new ArrayList<>();
        List<XjrBaseUser> cacheList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.USER_LIST_CACHE_KEY);
        if (CollectionUtil.isNotEmpty(userList) && CollectionUtil.isNotEmpty(cacheList)) {
            for (XjrBaseUser user : userList) {
                boolean isDuplicatedEnCode = false;
                boolean isDuplicatedAccount = false;
                boolean isDuplicatedMobile = false;
                boolean isDuplicatedEmail = false;
                boolean isDuplicatedWeChat = false;
                String enCode = user.getEnCode();
                String account = user.getAccount();
                String mobile = user.getMobile();
                String email = user.getEmail();
                String weChat = user.getWeChat();
                for (XjrBaseUser cache : cacheList) {
                    if (!StringUtil.isEmpty(enCode) && StringUtil.equals(enCode, cache.getEnCode())) {
                        isDuplicatedEnCode = true;
                    }
                    if (!StringUtil.isEmpty(account) && StringUtil.equals(account, cache.getAccount())) {
                        isDuplicatedAccount = true;
                    }
                    if (!StringUtil.isEmpty(mobile) && StringUtil.equals(mobile, cache.getMobile())) {
                        isDuplicatedMobile = true;
                    }
                    if (!StringUtil.isEmpty(email) && StringUtil.equals(email, cache.getEmail())) {
                        isDuplicatedEmail = true;
                    }
                    if (!StringUtil.isEmpty(weChat) && StringUtil.equals(weChat, cache.getWeChat())) {
                        isDuplicatedWeChat = true;
                    }
                }
                if(!isDuplicatedEnCode) {
                    for (XjrBaseUser checkedUser : userList) {
                        if (StringUtil.equals(user.getUserId(), checkedUser.getUserId())) {
                            continue;
                        }
                        if (!StringUtil.isEmpty(enCode) && StringUtil.equals(enCode, checkedUser.getEnCode())) {
                            isDuplicatedEnCode = true;
                        }
                        if (!StringUtil.isEmpty(account) && StringUtil.equals(account, checkedUser.getAccount())) {
                            isDuplicatedAccount = true;
                        }
                        if (!StringUtil.isEmpty(mobile) && StringUtil.equals(mobile, checkedUser.getMobile())) {
                            isDuplicatedMobile = true;
                        }
                        if (!StringUtil.isEmpty(email) && StringUtil.equals(email, checkedUser.getEmail())) {
                            isDuplicatedEmail = true;
                        }
                        if (!StringUtil.isEmpty(weChat) && StringUtil.equals(weChat, checkedUser.getWeChat())) {
                            isDuplicatedWeChat = true;
                        }
                    }
                }
                if (isDuplicatedEnCode) {
                    errorMsgList.add("编码存在重复：" + enCode);
                }
                if (isDuplicatedAccount) {
                    errorMsgList.add("账号存在重复：" + account);
                }
                if (isDuplicatedMobile) {
                    errorMsgList.add("手机号存在重复：" + mobile);
                }
                if (isDuplicatedEmail) {
                    errorMsgList.add("邮箱存在重复：" + email);
                }
                if (isDuplicatedWeChat) {
                    errorMsgList.add("微信号存在重复：" + weChat);
                }
                if (!isDuplicatedEnCode && !isDuplicatedAccount && !isDuplicatedMobile && !isDuplicatedEmail && !isDuplicatedWeChat) {
                    resultList.add(user);
                }
            }
        }
        return resultList;
    }
}
