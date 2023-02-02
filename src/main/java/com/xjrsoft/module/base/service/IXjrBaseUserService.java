package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.vo.UserInfoVo;
import com.xjrsoft.module.base.vo.UserVo;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
public interface IXjrBaseUserService extends IService<XjrBaseUser> {

    XjrBaseUser userInfo(String account, String password);

    List<XjrBaseDepartment> queryDepartmentsOfUser(String userId);

    List<XjrBaseRole> queryRolesOfUser(String userId);

    List<XjrBasePost> queryPostsOfUser(String userId);

    PageOutput<UserVo> pageList(GetPageListDto pageListDto, String companyId, String departmentId);

    boolean addUser(XjrBaseUser user, String departmentIds);

    XjrBaseUser getUserByMobile(String mobile);

    XjrBaseUser getUserByEnCode(String mobile);

    List<XjrBaseUser> getAllUserList();

    List<UserInfoVo> getMultiUsersInfo(List<String> userIds);

    void checkUser(String account, String mobile, String userId);
}
