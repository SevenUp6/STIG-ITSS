package com.xjrsoft.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.vo.DepartmentSimpleVo;
import com.xjrsoft.module.base.vo.UserInfoVo;
import com.xjrsoft.module.base.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
public interface XjrBaseUserMapper extends BaseMapper<XjrBaseUser> {

    List<XjrBaseDepartment> queryDepartmentsOfUser(String userId);

    List<XjrBaseRole> queryRolesOfUser(String userId);

    List<XjrBasePost> queryPostsOfUser(String userId);

    List<UserVo> pageList(String companyId, String departmentId, String keyword, IPage<XjrBaseUser> page);

    List<UserInfoVo> getMultiUsersInfo(List<String> userIds);

    List<DepartmentSimpleVo> getDepartmentSimpleVo(@Param("F_UserId") String userId);
}
