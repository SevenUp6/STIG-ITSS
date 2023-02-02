package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.Enum.DeleteMarkEnum;
import com.xjrsoft.common.allenum.EnabledMarkEnum;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.DigestUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.mapper.XjrBaseUserMapper;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.UserInfoVo;
import com.xjrsoft.module.base.vo.UserVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@Service
@AllArgsConstructor
public class XjrBaseUserServiceImpl extends ServiceImpl<XjrBaseUserMapper, XjrBaseUser> implements IXjrBaseUserService {

    private final IXjrBaseUserRelationService userRelationService;

    @Override
    public XjrBaseUser userInfo(String account, String password) {
        Wrapper<XjrBaseUser> wrapper = Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getPassword, password)
                .and(subWrapper -> subWrapper.eq(!StringUtil.isEmpty(account), XjrBaseUser::getAccount, account).or(!StringUtil.isEmpty(account)).eq(XjrBaseUser::getMobile, account).eq(XjrBaseUser::getDeleteMark,DeleteMarkEnum.NODELETE.getCode()).eq(XjrBaseUser::getEnabledMark,EnabledMarkEnum.ENABLED.getCode()));
        return this.getOne(wrapper);
    }

    public XjrBaseUser getUserByMobile(String mobile) {
        Wrapper<XjrBaseUser> wrapper = Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getMobile, mobile);
        return this.getOne(wrapper, false);
    }

    public XjrBaseUser getUserByEnCode(String enCode) {
        Wrapper<XjrBaseUser> wrapper = Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getEnCode, enCode);
        return this.getOne(wrapper, false);
    }

    @Override
    public List<XjrBaseUser> getAllUserList() {
        return this.list(Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getEnabledMark, 1));
    }

    @Override
    public List<XjrBaseDepartment> queryDepartmentsOfUser(String userId) {
        return baseMapper.queryDepartmentsOfUser(userId);
    }

    @Override
    public List<XjrBasePost> queryPostsOfUser(String userId) {
        return baseMapper.queryPostsOfUser(userId);
    }

    @Override
    public List<XjrBaseRole> queryRolesOfUser(String userId) {
        return baseMapper.queryRolesOfUser(userId);
    }

    @Override
    public PageOutput<UserVo> pageList(GetPageListDto pageListDto, String companyId, String departmentId) {
        String keyword = pageListDto.getKeyword();
        IPage<XjrBaseUser> page = ConventPage.getPage(pageListDto);
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        List<UserVo> userVoList = this.baseMapper.pageList(companyId, departmentId, keyword, page);
        // 去重
        List<UserVo> distinctUserVoList = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        for (UserVo userVo : userVoList) {
            String userId = userVo.getUserId();
            if (!userIds.contains(userId)) {
                userIds.add(userId);
                distinctUserVoList.add(userVo);
            }
        }
        return  ConventPage.getPageOutput(page.getTotal(), distinctUserVoList);
    }

    @Override
    public boolean addUser(XjrBaseUser user, String departmentIds) {
        this.checkUser(user.getAccount(), user.getMobile(), user.getUserId());
        // 密码MD5加密
        String password = user.getPassword();
        if (!StringUtil.isEmpty(password)) {
            user.setPassword(DigestUtil.encrypt(password));
        }
        boolean isSuccess = this.save(user) && OrganizationCacheUtil.addCache(OrganizationCacheUtil.USER_LIST_CACHE_KEY, user);
        if (isSuccess && !StringUtil.isEmpty(departmentIds)) {
            List<String> departmentList = StringUtil.split(departmentIds, ',', true, true);
            isSuccess = userRelationService.addUserRelationsForUser(user.getUserId(), 3, departmentList, false);
        }
        return isSuccess;
    }

    @Override
    public List<UserInfoVo> getMultiUsersInfo(List<String> userIds) {
        return this.baseMapper.getMultiUsersInfo(userIds);
    }

    /**
     *  检查手机号和账号是否重复
     * @param account 账号
     * @param mobile 手机号
     * @param userId 用户ID
     */
    public void checkUser(String account, String mobile, String userId) {
        if (StringUtil.isEmpty(account) && StringUtil.isEmpty(mobile)) {
            return;
        }
        if (this.count(Wrappers.<XjrBaseUser>query().lambda().ne(!StringUtil.isEmpty(userId), XjrBaseUser::getUserId, userId)
                .and(wrapper -> wrapper.eq(!StringUtil.isEmpty(account), XjrBaseUser::getAccount, account)
                        .or(!StringUtil.isEmpty(mobile)).eq(XjrBaseUser::getMobile, mobile))) > 0) {
            throw new RuntimeException("账号或者手机号已存在！");
        }
    }

    @Override
    public boolean save(XjrBaseUser user) {
        this.checkUser(user.getAccount(), user.getMobile(), user.getUserId());
        return super.save(user);
    }

    @Override
    public boolean updateById(XjrBaseUser user) {
        this.checkUser(user.getAccount(), user.getMobile(), user.getUserId());
        return super.updateById(user);
    }
}
