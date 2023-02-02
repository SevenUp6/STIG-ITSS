package com.xjrsoft.module.sync.controller;

import com.sap.conn.jco.*;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseDepartmentService;
import com.xjrsoft.module.base.service.IXjrBasePostService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.sync.constants.SapConstant;
import com.xjrsoft.module.sync.utils.SapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author jobob
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/sync")
@AllArgsConstructor
@Api(value = "/sync",tags = "组织架构信息同步")
public class SyncController {

    private IXjrBaseDepartmentService departmentService;

    private IXjrBasePostService postService;

    private IXjrBaseUserService userService;

    @GetMapping("/department")
    @ApiOperation(value = "同步部门信息")
    public Response syncCompany() {
        // 请求接口获取数据
        JCoTable tableList = SapUtil.getTableList(SapConstant.SAP_COMPANY_FUNCTION, SapConstant.SAP_COMPANY_TABLENAME, null);
        // 转换成xjr实体
        List<XjrBaseDepartment> departmentList = SapUtil.toXjrEntities(SapConstant.XJR_COMPANY_FIELDS, SapConstant.SAP_COMPANY_FIELDS, tableList, XjrBaseDepartment.class);
        return Response.status(departmentService.saveOrUpdateBatch(departmentList));
    }

    @GetMapping("/post")
    @ApiOperation(value = "同步岗位信息")
    public Response syncPost() {
        // 请求接口获取数据
        JCoTable tableList = SapUtil.getTableList(SapConstant.SAP_POST_FUNCTION, SapConstant.SAP_POST_TABLENAME, null);
        // 转换成xjr实体
        List<XjrBasePost> postList = SapUtil.toXjrEntities(SapConstant.XJR_POST_FIELDS, SapConstant.SAP_POST_FIELDS, tableList, XjrBasePost.class);
        return Response.status(postService.saveOrUpdateBatch(postList));
    }

    @GetMapping("/user")
    @ApiOperation(value = "同步公司信息")
    public Response syncUser() {
        // 请求接口获取数据
        JCoTable tableList = SapUtil.getTableList(SapConstant.SAP_USER_FUNCTION, SapConstant.SAP_USER_TABLENAME, null);
        // 转换成xjr实体
        List<XjrBaseUser> userList = SapUtil.toXjrEntities(SapConstant.XJR_USER_FIELDS, SapConstant.SAP_USER_FIELDS, tableList, XjrBaseUser.class);
        return Response.status(userService.saveOrUpdateBatch(userList));
    }
}
