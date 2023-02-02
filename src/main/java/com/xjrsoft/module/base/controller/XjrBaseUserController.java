package com.xjrsoft.module.base.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.RedisUtil;
import com.xjrsoft.core.constant.RedisKeyConstants;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.dto.PasswordDto;
import com.xjrsoft.module.base.dto.PhoneCodeDto;
import com.xjrsoft.module.base.dto.UserDto;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.UserInfoVo;
import com.xjrsoft.module.base.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Api(value = "/users",tags = "用户模块")
public class XjrBaseUserController {

    private RedisUtil redisUtil;

    private IXjrBaseUserService userService;

    private IXjrBaseUserRelationService userRelationService;

    @GetMapping("/page")
    @ApiOperation(value = "获取用户列表数据，分页")
    public Response<PageOutput<UserVo>> pageList(GetPageListDto pageListDto, @RequestParam(name = "Company_Id") String companyId,
                                                 @RequestParam(name = "Department_Id", required = false) String departmentId) {
        return Response.ok(userService.pageList(pageListDto, null, departmentId));
    }

    @PostMapping
    @ApiOperation(value = "新增用户")
    public Response addUser(@RequestBody UserDto userDto) {
        XjrBaseUser user = BeanUtil.copy(userDto, XjrBaseUser.class);
        return Response.status(userService.addUser(user, userDto.getDepartmentId()));
    }

    @PutMapping("/{id}")
    @ApiOperation(value ="修改用户")
    public Response updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        XjrBaseUser user = BeanUtil.copy(userDto, XjrBaseUser.class);
        user.setUserId(id);
        boolean isSuccess = userService.updateById(user) && OrganizationCacheUtil.updateCache(OrganizationCacheUtil.USER_LIST_CACHE_KEY, user);
        String departmentIds = userDto.getDepartmentId();
        if (isSuccess && !StringUtil.isEmpty(departmentIds)) {
            List<String> departmentList = StringUtil.split(departmentIds, ',', true, true);
            isSuccess = userRelationService.addUserRelationsForUser(id, 3, departmentList, true);
        }
        return Response.status(isSuccess);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除用户")
    public Response deleteUser(@PathVariable String ids) {
        String[] userIdArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (userIdArray.length == 1) {
            isSuccess = userService.removeById(ids);
        } else {
            isSuccess = userService.removeByIds(Arrays.asList(userIdArray));
        }
        if (isSuccess) {
            OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.USER_LIST_CACHE_KEY, userIdArray);
        }
        return Response.status(isSuccess);
    }

    @PatchMapping("/{id}/enabled")
    @ApiOperation(value = "修改启用状态")
    public Response enabledUser(@PathVariable String id) {
        XjrBaseUser user = userService.getById(id);
        boolean isSuccess = false;
        if (user != null) {
            XjrBaseUser updatedUser = new XjrBaseUser();
            updatedUser.setUserId(user.getUserId());
            updatedUser.setEnabledMark(user.getEnabledMark() == 0 ? 1 : 0);
            isSuccess = userService.updateById(updatedUser);
        }
        return Response.status(isSuccess);
    }

    @PatchMapping("/{id}/password")
    @ApiOperation(value = "重置密码")
    public Response restPassword(@PathVariable String id) {
        XjrBaseUser user = new XjrBaseUser();
        user.setUserId(id);
        user.setPassword(DigestUtil.encrypt("000000"));
        return Response.status(userService.updateById(user));
    }

    @PutMapping("/{id}/updatePassword")
    @ApiOperation(value = "修改密码")
    public Response updatePassword(@PathVariable String id, @RequestBody PasswordDto passwordDto) {
        int count = userService.count(Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getUserId, id).eq(XjrBaseUser::getPassword, passwordDto.getOldPassword()));
        if (count > 0) {
            XjrBaseUser user = new XjrBaseUser();
            user.setUserId(id);
            user.setPassword(passwordDto.getNewPassword());
            return Response.status(userService.updateById(user));
        }
        return Response.notOk("旧密码错误");
    }

    @PostMapping("/{id}/updatePassword")
    @ApiOperation(value = "修改密码")
    public Response updatePasswordP(@PathVariable String id,@RequestBody PasswordDto passwordDto) {
        int count = userService.count(Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getUserId, id).eq(XjrBaseUser::getPassword, passwordDto.getOldPassword()));
        if (count > 0) {
            XjrBaseUser user = new XjrBaseUser();
            user.setUserId(id);
            user.setPassword(passwordDto.getNewPassword());
            return Response.status(userService.updateById(user));
        }
        return Response.notOk("旧密码错误");
    }

    @GetMapping("/multi/{ids}")
    @ApiOperation(value = "批量查询用户信息 id逗号隔开(id1,id2)")
    public Response<List<UserInfoVo>> getMultiUsers(@PathVariable String ids) {
        String[] userIds = StringUtils.split(ids, StringPool.COMMA);
        return Response.ok(userService.getMultiUsersInfo(Arrays.asList(userIds)));
    }

    @PostMapping("/forget-password")
    @ApiOperation(value = "忘记密码，修改密码")
    public Response forgetPassword(@RequestBody PhoneCodeDto phoneCodeDto) {
        String mobile = phoneCodeDto.getMobile();
        if (redisUtil.getExpire(RedisKeyConstants.CACHE_SMS_CODE_PREFIX + mobile) > System.currentTimeMillis()) {
            return Response.notOk("验证码已过期！");
        }
        String cacheSmsCode = Func.toStr(redisUtil.get(RedisKeyConstants.CACHE_SMS_CODE_PREFIX + mobile));
        if (!StringUtil.equals(cacheSmsCode, phoneCodeDto.getCode())) {
            return Response.notOk("验证码错误！");
        }
        String newPassword = phoneCodeDto.getPassword();
        XjrBaseUser oldUser = userService.getUserByMobile(mobile);
        if (oldUser != null) {
            XjrBaseUser user = new XjrBaseUser();
            user.setUserId(oldUser.getUserId());
            user.setPassword(newPassword);
            return Response.status(userService.updateById(user));
        }
        return Response.notOk("用户不存在！");
    }

    @PostMapping("/{id}/head-icon")
    @ApiOperation(value = "忘记密码，修改密码")
    public Response uploadHeadIcon(@PathVariable String id, @RequestParam(value = "file", required = true)MultipartFile multipartFile) {
        String dirPath = IoUtil.getProjectPath() + File.separator + "static" + File.separator + "headIcons";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        XjrBaseUser user = userService.getById(id);
        if (user == null) {
            return Response.notOk("用户不存在");
        }
        File uploadFile = IoUtil.toFile(multipartFile);
        String suffix = StringPool.DOT + StringUtils.substringAfterLast(uploadFile.getName(), StringPool.DOT);
        String filePath = dirPath + File.separator + user.getUserId() + suffix;
        File file = new File(filePath);
        FileUtil.move(uploadFile, file, true);
        XjrBaseUser updateUser = new XjrBaseUser();
        updateUser.setUserId(user.getUserId());
        updateUser.setHeadIcon(suffix);
        return Response.status(userService.updateById(updateUser));
    }

    @GetMapping("/{id}/head-icon")
    @ApiOperation(value = "忘记密码，修改密码")
    public void getHeadIcon(@PathVariable String id) throws IOException {
        XjrBaseUser user = userService.getById(id);
        String filePath = IoUtil.getProjectPath() + File.separator + "static" + File.separator + "headIcons" + File.separator + user.getUserId() + user.getHeadIcon();
        File file = new File(filePath);
        if (file.exists()) {
            WebUtil.writeFileToResponse(file.getName(), file);
        }
//        return Response.notOk("头像不存在！");
    }

    public  List<UserInfoVo> getUserInfo(String id) {
//        String[] userIds = StringUtils.split(id, StringPool.COMMA);
        return userService.getMultiUsersInfo(Arrays.asList(id));
    }
}
