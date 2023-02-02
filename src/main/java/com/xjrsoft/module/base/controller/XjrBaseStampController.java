package com.xjrsoft.module.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.module.base.dto.GetStampPageListDto;
import com.xjrsoft.module.base.dto.SaveStampDto;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.entity.XjrBaseStamp;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseRoleService;
import com.xjrsoft.module.base.service.IXjrBaseStampService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:光华科技-软件研发部
 * @Date:2020/11/4
 * @Description:工作流签章控制器
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/stamps")
@Api(value = "/stamps",tags = "签章模块")
public class XjrBaseStampController {

    private IXjrBaseStampService stampService;

    private IXjrBaseRoleService roleService;

    private IXjrBaseUserService userService;


    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/5
    * @Param:[dto, StampType:分类, EnabledMark:启用状态, StampAttributes:0私人签章 1 默认签章 2 公共签章]
    * @return:com.xjrsoft.common.result.Response<com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.base.entity.XjrBaseStamp>>
    * @Description:获取签章列表(分页)
    */
    @GetMapping()
    @SneakyThrows
    @ApiOperation(value="获取签章列表(分页)")
    public Response<PageOutput<XjrBaseStamp>> queryStampsPageData(GetStampPageListDto dto) {
        // 获取当前登录用户
        XjrUser currentUser = SecureUtil.getUser();
        return Response.ok(stampService.getStampPageList(dto, currentUser.getUserId()));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/4
     * @Param:[xjrBaseStamp]
     * @return:com.xjrsoft.common.result.Response
     * @Description:新增签章
     */
    @PostMapping()
    @ApiOperation(value="新增签章")
    public Response saveStamp(@RequestBody SaveStampDto dto) {
        return Response.status(stampService.saveStamp(dto));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/4
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:修改签章
     */
    @PutMapping("/{id}")
    @ApiOperation(value="修改")
    public Response updateStamp(@PathVariable String id, @RequestBody SaveStampDto xjrBaseStamp) {
        if (stampService.checkMaintainUser(id)) {
            return Response.status(stampService.updateStamp(id, xjrBaseStamp));
        }
           return Response.notOk("非管理员账号不能操作公共签章!");
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/5
    * @Param:[ids]
    * @return:com.xjrsoft.common.result.Response
    * @Description:删除签章(物理删除)
    */
    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除签章(物理删除)")
    public Response deleteStamps(@PathVariable String ids) {
        if (stampService.checkMaintainUser(ids)) {
            return Response.status(stampService.removeByIds(Arrays.asList(ids.split(","))));
        }
        return Response.notOk("非管理员账号不能操作公共签章!");
    }

    @PatchMapping("/{id}/enable")
    @ApiOperation(value="启用")
    @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "path")
    public Response start(@PathVariable("id") String id) {
        if (stampService.checkMaintainUser(id)) {
            XjrBaseStamp xjrBaseStamp = stampService.getById(id);
            if (xjrBaseStamp != null) {
                xjrBaseStamp.setEnabledMark(1);
                boolean flag = stampService.updateById(xjrBaseStamp);
                if (flag) {
                    return Response.status(true);
                }
            }
            return Response.status(false);
        }else {
            return Response.notOk("非管理员账号不能操作公共签章!");
        }
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/5
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:停用
    */
    @PatchMapping("/{id}/not-enabled")
    @ApiOperation(value="停用")
    @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "path")
    public Response stop(@PathVariable("id") String id) {
        if (stampService.checkMaintainUser(id)) {
            XjrBaseStamp xjrBaseStamp = stampService.getById(id);
            if (xjrBaseStamp != null) {
                xjrBaseStamp.setEnabledMark(0);
                return Response.status(stampService.updateById(xjrBaseStamp));
            } else {
                return Response.status(false);
            }
        }else {
            return Response.notOk("非管理员账号不能操作公共签章!");
        }
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/5
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:设定为默认签章
    */
    @PatchMapping("/{id}/set-default")
    @ApiOperation(value="设定为默认签章")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "path")
    public Response setDefault(@PathVariable String id) {
        if (stampService.checkMaintainUser(id)) {
            XjrUser currentUser = SecureUtil.getUser();
            return Response.status(stampService.setDefaultStamp(id, currentUser.getUserId()));
        }else {
            return Response.notOk("非管理员账号不能操作公共签章!");
        }
    }

    /* 公共签章部分 */

    /**
    * @Author:光华科技-软件研发部
    * @Date:2021/3/15
    * @Param:[]
    * @return:com.xjrsoft.common.result.Response
    * @Description:查询公共签章下的授权成员(需要管理员角色才有权限)
    */
    @GetMapping("/{id}/public-stamp")
    @ApiOperation(value="查询公共签章下的授权成员")
    public Response selectUserOfPStamp(@PathVariable String id) {
        JSONArray userArray = new JSONArray();
        boolean isAdminRole = false;
        String currentUserId = SecureUtil.getUserId();
        XjrBaseRole sysRole = roleService.getSysRole();
        List<XjrBaseRole> rolesOfUserId = roleService.getRolesByUserId(currentUserId);
        // 判断权限
        for (XjrBaseRole xjrBaseRole : rolesOfUserId) {
            if (StringUtils.equals(xjrBaseRole.getRoleId(), sysRole.getRoleId())) {
                isAdminRole = true;
                break;
            }
        }
        if (isAdminRole) {
            XjrBaseStamp xjrBaseStamp = stampService.getById(id);
            if (xjrBaseStamp != null) {
                String authorizeUserStr = xjrBaseStamp.getAuthorizeUser();
                JSONArray authorizeUser = JSON.parseArray(authorizeUserStr);
                if(authorizeUser!=null) {
                    for (int i = 0; i < authorizeUser.size(); i++) {
                        String userId = authorizeUser.getString(i);
                        XjrBaseUser xjrBaseUser = userService.getById(userId);
                        if (xjrBaseUser != null) {
                            userArray.add(xjrBaseUser);
                        }
                    }
                }
            }
            return Response.ok(userArray);
        } else {
            return Response.ok("非管理员账号不能操作公共签章!");
        }
    }

    @PostMapping("/{id}/public-stamp")
    @ApiOperation(value="添加公共签章授权用户")
    public Response addUserOfPStamp(@PathVariable String id, @RequestBody String authorizeUser) {
        boolean isAdminRole = false;
        String currentUserId = SecureUtil.getUserId();
        XjrBaseRole sysRole = roleService.getSysRole();
        List<XjrBaseRole> rolesOfUserId = roleService.getRolesByUserId(currentUserId);
        // 判断权限
        for (XjrBaseRole xjrBaseRole : rolesOfUserId) {
            if (StringUtils.equals(xjrBaseRole.getRoleId(), sysRole.getRoleId())) {
                isAdminRole = true;
                break;
            }
        }
        if (isAdminRole) {
//             添加授权用户
            return Response.status(stampService.addAuthorizeUser(id, authorizeUser));
        } else {
            return Response.notOk("非管理员账号不能操作公共签章!");
        }
    }

    //查询成员
    @GetMapping("/{id}/member")
    @ApiOperation(value="查询成员")
    public Response selectMember(@PathVariable String id) {
        JSONArray userArray = new JSONArray();
        if (stampService.checkMaintainUser(id)) {
        XjrBaseStamp xjrBaseStamp = stampService.getById(id);
        if (xjrBaseStamp != null) {
            String member = xjrBaseStamp.getMember();
            JSONArray memberUser = JSON.parseArray(member);
            if(memberUser!=null) {
                for (int i = 0; i < memberUser.size(); i++) {
                    String userId = memberUser.getString(i);
                    XjrBaseUser xjrBaseUser = userService.getById(userId);
                    if (xjrBaseUser != null) {
                        JSONObject userInfo = new JSONObject();
                        userInfo.put("F_UserId",xjrBaseUser.getUserId());
                        userInfo.put("F_Account",xjrBaseUser.getAccount());
                        userInfo.put("F_RealName",xjrBaseUser.getRealName());
                        userArray.add(userInfo);
                    }
                }
            }
        }
        return Response.ok(userArray);
        } else {
            return Response.notOk("非维护人员不能操作公共签章!");
        }
    }


    //添加成员
    @PutMapping("/{id}/member")
    @ApiOperation(value="添加成员")
    public Response addMember(@PathVariable String id, @RequestBody String member) {
        if (stampService.checkMaintainUser(id)) {
//         添加成员
        return Response.status(stampService.addMember(id, member));
        } else {
            return Response.notOk("非维护人员不能操作公共签章!");
        }
    }


    //查询维护成员
    @GetMapping("/{id}/maintain")
    @ApiOperation(value="查询维护成员")
    public Response selectMaintain(@PathVariable String id) {
        JSONArray userArray = new JSONArray();
            XjrBaseStamp xjrBaseStamp = stampService.getById(id);
            if (xjrBaseStamp != null) {
                String maintain = xjrBaseStamp.getMaintain();
                JSONArray maintainUser = JSON.parseArray(maintain);
                if(maintainUser!=null) {
                    for (int i = 0; i < maintainUser.size(); i++) {
                        String userId = maintainUser.getString(i);
                        XjrBaseUser xjrBaseUser = userService.getById(userId);
                        if (xjrBaseUser != null) {
                            JSONObject userInfo = new JSONObject();
                            userInfo.put("F_UserId",xjrBaseUser.getUserId());
                            userInfo.put("F_Account",xjrBaseUser.getAccount());
                            userInfo.put("F_RealName",xjrBaseUser.getRealName());
                            userArray.add(userInfo);
                        }
                    }
                }
            }
            return Response.ok(userArray);
    }


    //添加维护成员
    @PutMapping("/{id}/maintain")
    @ApiOperation(value="添加维护用户")
    public Response addMaintain(@PathVariable String id, @RequestBody String maintain) {
//         添加成员
        if (stampService.checkMaintainUser(id)) {
            return Response.status(stampService.addMaintain(id, maintain));
        }else {
            return Response.notOk("非维护人员不能操作公共签章!");
        }
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2021/3/16
    * @Param:[id]
    * @return:com.xjrsoft.common.result.Response
    * @Description:获取签章的图片
    */
    @GetMapping("/stamp-img")
    @ApiOperation(value="获取签章的图片")
    public Response getStampImg(String id){
        XjrBaseStamp xjrBaseStamp = stampService.getById(id);
        if (xjrBaseStamp != null) {
            return Response.ok(xjrBaseStamp.getImgFile());
        } else {
            return Response.ok("签章不存在!");
        }
    }

}
