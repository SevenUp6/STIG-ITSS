package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.dto.DepartmentDto;
import com.xjrsoft.module.base.dto.MemberUserIdDto;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.service.IXjrBaseDepartmentService;
import com.xjrsoft.module.base.service.IXjrBaseSpecialPostService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.MemberUserVo;
import com.xjrsoft.module.base.vo.SpecialPostVo;
import com.xjrsoft.module.base.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门信息表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/departments")
@Api(value = "/departments",tags = "部门模块")
public class XjrBaseDepartmentController {

    private IXjrBaseDepartmentService departmentService;

    private IXjrBaseSpecialPostService specialPostService;

    private IXjrBaseUserRelationService userRelationService;

    @GetMapping("/{id}/users")
    @ApiOperation(value="获取部门下的人员，不分页")
    @ApiImplicitParam(name = "部门id", value = "id", required = true, dataType = "string")
    public Response<List<MemberUserVo>> getUsersOfDepartment(@PathVariable String id, @RequestParam(value = "keyword", required = false) String keyword){
        List<UserVo> userVoList = userRelationService.getMemberUserVoListOfObject(id, 3,keyword);
        List<MemberUserVo> memberUserVoList = BeanUtil.copyList(userVoList, MemberUserVo.class);
        return Response.ok(memberUserVoList, "");
    }

    @PostMapping
    @ApiOperation(value="新增部门")
    public Response saveDepartment(@RequestBody DepartmentDto departmentDto){
        Map<String, List<String>> postUserJson = departmentDto.getPostUserJson();
        XjrBaseDepartment department = BeanUtil.copy(departmentDto, XjrBaseDepartment.class);
        return Response.status(departmentService.addDepartment(department, postUserJson));
    }

    @GetMapping("/{id}/special-post")
    @ApiOperation(value="查询部门特殊岗位")
    @ApiImplicitParam(name = "部门id", value = "id", required = true, dataType = "string")
    public Response<List<SpecialPostVo>> getSpecialPost(@PathVariable String id){
        List<SpecialPostVo> specialPostVoList = specialPostService.getSpecialPost(id, 5, 2);
        return Response.ok(specialPostVoList, "查询成功！");
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改部门")
    @ApiImplicitParam(name = "部门id", value = "id", required = true, dataType = "string")
    public Response updateDepartment(@PathVariable String id, @RequestBody DepartmentDto departmentDto){
        Map<String, List<String>> postUserJson = departmentDto.getPostUserJson();
        XjrBaseDepartment department = BeanUtil.copy(departmentDto, XjrBaseDepartment.class);
        return Response.status(departmentService.updateDepartment(id, department, postUserJson));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除部门")
    @ApiImplicitParam(name = "部门id,多个用逗号隔开", value = "ids", required = true, dataType = "string")
    public Response deleteDepartment(@PathVariable String ids) {
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idsArray.length > 1) {
            isSuccess = departmentService.removeByIds(Arrays.asList(idsArray));
        } else {
            isSuccess = departmentService.removeById(ids);
        }
        if (isSuccess) {
            OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, idsArray);
        }
        return Response.status(isSuccess);
    }

    @PostMapping("/users")
    @ApiOperation(value="添加部门的角色")
    public Response addUsersForDepartment(@RequestBody MemberUserIdDto memberUserIdDto) {
        String departmentId = memberUserIdDto.getDepartmentId();
        List<String> userIds = memberUserIdDto.getUserIdList();
        return Response.status(userRelationService.addUserRelationsForObject(departmentId, 3, userIds));
    }

    @GetMapping("/{id}")
    @ApiOperation(value="获取部门信息")
    public Response getDepartmentInfo(@PathVariable String id) {
        XjrBaseDepartment xjrBaseDepartment = departmentService.getById(id);
       return Response.ok(xjrBaseDepartment);
    }
}
