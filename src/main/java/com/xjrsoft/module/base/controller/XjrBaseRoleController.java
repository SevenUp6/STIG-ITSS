package com.xjrsoft.module.base.controller;

import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.dto.MemberUserIdDto;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.service.IXjrBaseRoleService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.MemberUserVo;
import com.xjrsoft.module.base.vo.RoleVo;
import com.xjrsoft.module.base.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:湘北智造-框架开发组
 * @Date:2020/11/4
 * @Description:角色控制器
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/roles")
@Api(value = "/roles",tags = "角色模块")
public class XjrBaseRoleController {

    private final IXjrBaseUserRelationService userRelationService;

    private final IXjrBaseRoleService roleService;

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/4
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取角色详情
    */
    @GetMapping("/{id}")
    @ApiOperation(value="获取角色详情")
    @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "string")
    public Response<XjrBaseRole> getRoleById(@PathVariable String id){
        return Response.ok(roleService.getById(id));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/4
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:修改角色
    */
    @PutMapping("/{id}")
    @ApiOperation(value="修改")
    public Response updateRole(@PathVariable String id, @RequestBody  XjrBaseRole role) {
        role.setRoleId(id);
        return Response.status(roleService.updateById(role) && OrganizationCacheUtil.updateCache(OrganizationCacheUtil.ROLE_LIST_CACHE_KEY, role));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/4
    * @Param:[dto]
    * @return:com.xjrsoft.common.result.Response<com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.base.entity.XjrBaseRole>>
    * @Description:获取角色列表(分页)
    */
    @GetMapping("/page")
    @ApiOperation(value="获取角色列表(分页)")
    public Response<PageOutput<RoleVo>> queryRolesPageData(GetPageListDto dto) {
        return Response.ok(roleService.getRolePageList(dto));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/4
    * @Param:[dto]
    * @return:com.xjrsoft.common.result.Response
    * @Description:获取角色列表
    */
    @GetMapping
    @ApiOperation(value="获取角色列表")
    public Response queryRolesData() {
        return Response.ok(roleService.list());
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/4
    * @Param:[xjrBaseRole]
    * @return:com.xjrsoft.common.result.Response
    * @Description:新增角色
    */
    @PostMapping
    @ApiOperation(value="新增角色")
    public Response saveRole(@RequestBody  XjrBaseRole role) {
        return Response.status(roleService.save(role) && OrganizationCacheUtil.addCache(OrganizationCacheUtil.ROLE_LIST_CACHE_KEY, role));
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/4
    * @Param:[]
    * @return:com.xjrsoft.common.result.Response
    * @Description:
    */
    @GetMapping("/{id}/users")
    @ApiOperation(value="查看角色包含的人员")
    public Response<List<MemberUserVo>> queryUsersForRole(@PathVariable String id, @RequestParam(name = "keyword", required = false) String keyword) {
        List<UserVo> userVoList = userRelationService.getMemberUserVoListOfObject(id, 1, keyword);
        List<MemberUserVo> memberUserVoList = BeanUtil.copyList(userVoList, MemberUserVo.class);
        return Response.ok(memberUserVoList, "查询成功！");
    }

    @GetMapping("/getusers")
    @ApiOperation(value="查看角色包含的人员（过滤当前userid）")
    public Response<List<MemberUserVo>> queryUsersForRole(@RequestParam(name = "userid", required = false) String userid) {
        List<UserVo> userVoList = userRelationService.getMemberUserVoListOfObject("92156e8c512ea54605efa95a61044f55", 1, null);
        List<MemberUserVo> memberUserVoList = BeanUtil.copyList(userVoList, MemberUserVo.class);
//        for( MemberUserVo member:memberUserVoList){
//            if(userid.equals(member.getUserId())||userid== member.getUserId()) {
//                memberUserVoList.remove(member);
//                break;
//            }
//        }
        return Response.ok(memberUserVoList, "查询成功！");
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/5
    * @Param:[ids]
    * @return:com.xjrsoft.common.result.Response
    * @Description:删除角色(物理删除)
    */
    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除角色(物理删除)")
    public Response deleteRoles(@PathVariable String ids) {
        String[] idArray = ids.split(",");
        return Response.status(roleService.removeByIds(Arrays.asList(idArray)) && OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.ROLE_LIST_CACHE_KEY, idArray));
    }

    @PostMapping("/users")
    @ApiOperation(value="添加角色的人员")
    public Response addUsersForPost(@RequestBody MemberUserIdDto memberUserIdDto) {
        String roleId = memberUserIdDto.getRoleId();
        List<String> userIds = memberUserIdDto.getUserIdList();
        return Response.status(userRelationService.addUserRelationsForObject(roleId, 1, userIds));
    }
}
