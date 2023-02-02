package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseRole;
import com.xjrsoft.module.base.mapper.XjrBaseRoleMapper;
import com.xjrsoft.module.base.service.IXjrBaseRoleService;
import com.xjrsoft.module.base.vo.RoleVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@Service
public class XjrBaseRoleServiceImpl extends ServiceImpl<XjrBaseRoleMapper, XjrBaseRole> implements IXjrBaseRoleService {

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/6
    * @Param:[userId]
    * @return:java.util.List<java.lang.String>
    * @Description:查询用户的角色
    */
    public List<String> getRoleIdsForUser(String userId){
        return baseMapper.getRoleIdsForUser(userId);
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/4
    * @Param:[dto, StampType:分类, EnabledMark:启用状态, StampAttributes:0私人签章 1 默认签章 2 公共签章]
    * @return:com.xjrsoft.common.page.PageOutput<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取角色列表(分页)
    */
    @Override
    public PageOutput<RoleVo> getRolePageList(GetPageListDto dto) {
        QueryWrapper<XjrBaseRole> query = new QueryWrapper<>();
        query.select(XjrBaseRole.class, x -> VoToColumn.Convert(RoleVo.class).contains(x.getColumn()));
        query.lambda().like(!StrUtil.hasBlank(dto.getKeyword()),XjrBaseRole::getFullName,dto.getKeyword());
        IPage<XjrBaseRole> xjrBaseRoleIPage = this.page(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(xjrBaseRoleIPage, RoleVo.class);
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/6
    * @Param:[userId]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取用户的角色
    */
    @Override
    public List<XjrBaseRole> getRolesByUserId(String userId) {
        return baseMapper.getRolesByUserId(userId);
    }


    @Override
    public List<XjrBaseRole> getAppRolesByUserId(String userId){
        return baseMapper.getAppRolesByUserId(userId);
    }

    /**
    * @Author:光华科技-软件研发部
    * @Date:2021/1/13
    * @Param:[]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseRole>
    * @Description:获取超级管理员角色
    */
    @Override
    public XjrBaseRole getSysRole() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("F_EnCode", "system");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean isAdminRole(String roleId) {
        return this.count(Wrappers.<XjrBaseRole>query().lambda().eq(XjrBaseRole::getRoleId, roleId).eq(XjrBaseRole::getEnCode, "system")) > 0;
    }
}
