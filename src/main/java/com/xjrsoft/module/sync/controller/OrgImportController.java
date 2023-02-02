package com.xjrsoft.module.sync.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.sync.constants.ImportConstant;
import com.xjrsoft.module.sync.entity.ImportUser;
import com.xjrsoft.module.sync.utils.OrgImportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author jobob
 * @since 2021-07-05
 */
@Slf4j
@RestController
@RequestMapping("/org-import")
@AllArgsConstructor
@Api(value = "/org-import", tags = "组织架构数据导入")
public class OrgImportController {

    private final IXjrBaseCompanyService companyService;

    private final IXjrBaseDepartmentService departmentService;

    private final IXjrBasePostService postService;

    private final IXjrBaseRoleService roleService;

    private final IXjrBaseUserService userService;

    private final IXjrBaseUserRelationService userRelationService;

    private final ObjectMapper objectMapper;

    @PostMapping("/company")
    @ApiOperation(value = "公司数据导入")
    public Response importCompany(@RequestParam(value = "file") MultipartFile multipartFile) {
        File file = IoUtil.toFile(multipartFile);
        Set<String> errorMsgList = new LinkedHashSet<>();
        List<XjrBaseCompany> companyList = OrgImportUtil.parseExcelToEntities(file, ImportConstant.IMPORT_COMPANY_FIELDS, XjrBaseCompany.class, errorMsgList);
        //关联上级
        List<XjrBaseCompany> savedList = new ArrayList<>();
        Map<String, String> encodeKeyMap = new HashMap(companyList.size());
        for (XjrBaseCompany company : companyList) {
            String companyId = IdWorker.get32UUID();
            company.setCompanyId(companyId);
            encodeKeyMap.put(company.getEnCode(), companyId);
        }
        companyList.stream().forEach(company -> company.setParentId(StringUtils.defaultIfBlank(encodeKeyMap.get(company.getParentId()), "0")));
        return Response.status(companyService.saveBatch(companyList) && OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, companyList));
    }

    @PostMapping("/department")
    @ApiOperation(value = "部门数据导入")
    public Response importDepartment(@RequestParam(value = "file") MultipartFile multipartFile) {
        File file = IoUtil.toFile(multipartFile);
        Set<String> errorMsgList = new LinkedHashSet<>();
        List<XjrBaseDepartment> departmentList = OrgImportUtil.parseExcelToEntities(file, ImportConstant.IMPORT_DEPARTMENT_FIELDS, XjrBaseDepartment.class, errorMsgList);
        departmentList.forEach(department -> department.setDepartmentId(IdWorker.get32UUID()));
        //关联上级
        List<XjrBaseCompany> companyList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY);
        List<XjrBaseDepartment> savedList = new ArrayList<>();
        for (XjrBaseDepartment department : departmentList) {
            boolean haveCompany = false;
            String companyId = department.getCompanyId();
            if (!StringUtil.isEmpty(companyId)) {
                for (XjrBaseCompany company : companyList) {
                    if (StringUtil.equals(company.getEnCode(), companyId)) {
                        department.setCompanyId(company.getCompanyId());
                        haveCompany = true;
                    }
                }
            }
            // 检查上级
            boolean haveParent = false;
            if (StringUtil.isEmpty(department.getParentId())) {
                department.setParentId("0");
                haveParent = true;
            } else {
                List<XjrBaseDepartment> parentList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
                parentList.addAll(departmentList);
                for (XjrBaseDepartment parent : parentList) {
                    if (StringUtil.equals(parent.getEnCode(), department.getParentId())) {
                        department.setParentId(parent.getDepartmentId());
                        haveParent = true;
                        break;
                    }
                }
            }
            if (!haveCompany) {
                errorMsgList.add("公司不存在：" + companyId);
            }
            if (!haveParent) {
                errorMsgList.add("上级部门不存在：" + department.getParentId());
            }
            if (haveCompany && haveParent) {
                savedList.add(department);
            }
        }
        savedList = OrgImportUtil.checkDuplicateDepartment(savedList, errorMsgList);
        departmentService.saveBatch(savedList);
        if (CollectionUtil.isNotEmpty(savedList)) {
            OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, savedList);
        }
        String msg = "成功导入" + savedList.size() + "条， 失败：" + errorMsgList;
        return savedList.size() > 0 ? Response.ok("", msg) : Response.notOk(msg);
    }

    @PostMapping("/post")
    @ApiOperation(value = "岗位数据导入")
    public Response importPost(@RequestParam(value = "file") MultipartFile multipartFile) {
        File file = IoUtil.toFile(multipartFile);
        Set<String> errorMsgList = new LinkedHashSet<>();
        List<XjrBasePost> postList = OrgImportUtil.parseExcelToEntities(file, ImportConstant.IMPORT_POST_FIELDS, XjrBasePost.class, errorMsgList);
        postList.forEach(department -> department.setPostId(IdWorker.get32UUID()));
        List<XjrBaseDepartment> departmentList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
        List<XjrBasePost> savedList = new ArrayList<>();
        for (XjrBasePost post : postList) {
            boolean haveDepartment = false;
            String departmentId = post.getDepartmentId();
            if (!StringUtil.isEmpty(departmentId)) {
                for (XjrBaseDepartment department : departmentList) {
                    if (StringUtil.equals(department.getEnCode(), departmentId)) {
                        post.setDepartmentId(department.getDepartmentId());
                        post.setCompanyId(department.getCompanyId());
                        haveDepartment = true;
                    }
                }
            }
            // 检查上级
            boolean haveParent = false;
            if (StringUtil.isEmpty(post.getParentId())) {
                post.setParentId("0");
                haveParent = true;
            } else {
                List<XjrBasePost> parentList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.POST_LIST_CACHE_KEY);
                parentList.addAll(postList);
                for (XjrBasePost parent : parentList) {
                    if (StringUtil.equals(parent.getEnCode(), post.getParentId())) {
                        post.setParentId(parent.getPostId());
                        haveParent = true;
                        break;
                    }
                }
            }
            if (!haveDepartment) {
                errorMsgList.add("部门不存在：" + departmentId);
            }
            if (!haveParent) {
                errorMsgList.add("上级岗位不存在：" + post.getParentId());
            }
            if (haveDepartment && haveParent) {
                savedList.add(post);
            }
        }
        savedList = OrgImportUtil.checkDuplicatePost(savedList, errorMsgList);
        postService.saveBatch(savedList);
        if (CollectionUtil.isNotEmpty(savedList)) {
            OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.POST_LIST_CACHE_KEY, savedList);
        }
        String msg = "成功导入" + savedList.size() + "条， 失败：" + errorMsgList;
        return savedList.size() > 0 ? Response.ok("", msg) : Response.notOk(msg);
    }

    @PostMapping("/role")
    @ApiOperation(value = "角色数据导入")
    public Response importRole(@RequestParam(value = "file") MultipartFile multipartFile) {
        File file = IoUtil.toFile(multipartFile);
        Set<String> errorMsgList = new LinkedHashSet<>();
        List<XjrBaseRole> roleList = OrgImportUtil.parseExcelToEntities(file, ImportConstant.IMPORT_ROLE_FIELDS, XjrBaseRole.class, errorMsgList);
        return Response.status(roleService.saveBatch(roleList) && OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.ROLE_LIST_CACHE_KEY, roleList));
    }

    @PostMapping("/user")
    @ApiOperation(value = "用户数据导入")
    public Response importUser(@RequestParam(value = "file") MultipartFile multipartFile) {
        File file = IoUtil.toFile(multipartFile);
        Set<String> errorMsgList = new LinkedHashSet<>();
        List<ImportUser> importUserList = OrgImportUtil.parseExcelToEntities(file, ImportConstant.IMPORT_USER_FIELDS, ImportUser.class, errorMsgList);
        List<XjrBaseUserRelation> userRelationList = new ArrayList<>();
        List<XjrBaseUser> savedList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(importUserList)) {
            List<XjrBaseDepartment> departmentList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
            for (ImportUser importUser : importUserList) {
                XjrBaseUser user = BeanUtil.copy(importUser, XjrBaseUser.class);
                String departmentId = importUser.getDepartmentId();
                if (StringUtil.isEmpty(departmentId)) {
                    continue;
                }
                boolean haveDepartment = false;
                // 关联部门
                for (XjrBaseDepartment department : departmentList) {
                    if (StringUtil.equals(departmentId, department.getEnCode())) {
                        String userId = IdWorker.get32UUID();
                        user.setUserId(userId);
                        user.setCompanyId(department.getCompanyId());
                        XjrBaseUserRelation userRelation = new XjrBaseUserRelation();
                        userRelation.setCategory(3);
                        userRelation.setObjectId(department.getDepartmentId());
                        userRelation.setUserId(userId);
                        userRelationList.add(userRelation);
                        haveDepartment = true;
                    }

                }
                if (!StringUtil.isEmpty(departmentId) && !haveDepartment) {
                    errorMsgList.add("部门不存在：" + departmentId);
                } else {
                    savedList.add(user);
                }
            }
        }
        savedList = OrgImportUtil.checkDuplicateUser(savedList, errorMsgList);
        userService.saveBatch(savedList);
        userRelationService.saveBatch(userRelationList);
        if (CollectionUtil.isNotEmpty(savedList)) {
            OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.USER_LIST_CACHE_KEY, savedList);
        }
        if (CollectionUtil.isNotEmpty(userRelationList)) {
            OrganizationCacheUtil.addCacheList(OrganizationCacheUtil.USER_RELATION_LIST_CACHE_KEY, userRelationList);
        }
        String msg = "成功导入" + savedList.size() + "条， 失败：" + errorMsgList;
        return savedList.size() > 0 ? Response.ok("", msg) : Response.notOk(msg);
    }

    @GetMapping("/template/{type}")
    @ApiOperation(value = "用数据导入")
    public void downloadTemplate(@PathVariable String type) throws IOException {
        String filePath = IoUtil.getProjectPath() + File.separator + "src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "OrgImportTemplates" + File.separator + type + ".xls";
        File file = new File(filePath);
        HttpServletResponse response = WebUtil.getResponse();
        if (file == null) {
            try {
                String result = objectMapper.writeValueAsString(Response.notOk("文件不存在！"));
                response.getOutputStream().write(result.getBytes());
                return;
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }else {
            // 设置下载文件名
            WebUtil.writeFileToResponse(file.getName(), file);
        }
    }
}