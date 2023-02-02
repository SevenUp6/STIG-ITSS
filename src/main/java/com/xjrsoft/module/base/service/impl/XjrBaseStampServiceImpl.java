package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.GetStampPageListDto;
import com.xjrsoft.module.base.dto.SaveStampDto;
import com.xjrsoft.module.base.entity.XjrBaseStamp;
import com.xjrsoft.module.base.mapper.XjrBaseStampMapper;
import com.xjrsoft.module.base.service.IXjrBaseStampService;
import com.xjrsoft.module.base.vo.StampVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Author:湘北智造-框架开发组
* @Date:2020/10/26
* @Description:工作流签章服务实现类
*/
@Service
public class XjrBaseStampServiceImpl extends ServiceImpl<XjrBaseStampMapper, XjrBaseStamp> implements IXjrBaseStampService {

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/5
    * @Param:[dto, StampType:分类, EnabledMark:启用状态, StampAttributes:0私人签章 1 默认签章 2 公共签章]
    * @return:com.xjrsoft.common.page.PageOutput
    * @Description:获取签章列表(分页)
    */
    @Override
    public PageOutput getStampPageList(GetStampPageListDto dto, String currentUserId) {
        QueryWrapper<XjrBaseStamp> query = new QueryWrapper<>();
        if(StringUtils.equalsIgnoreCase(dto.getStampAttributes(), "3")){
            query.lambda().and(wrapper-> wrapper.in(XjrBaseStamp::getStampAttributes, new Integer[]{0,1}).eq(XjrBaseStamp::getCreateUserId, currentUserId))
                    .or(wrapper1-> wrapper1.eq(XjrBaseStamp::getStampAttributes, 2).like(XjrBaseStamp::getMember,SecureUtil.getUserId()));
        }else {
            query.lambda().like(!StrUtil.hasBlank(dto.getKeyword()), XjrBaseStamp::getStampName, dto.getKeyword())
                    .eq(!StrUtil.hasBlank(dto.getStampType()), XjrBaseStamp::getStampType, dto.getStampType())
                    .eq(!StrUtil.hasBlank(dto.getEnabledMark()), XjrBaseStamp::getEnabledMark, dto.getEnabledMark())
                    .eq(!StringUtils.equalsIgnoreCase(dto.getStampAttributes(), "2"), XjrBaseStamp::getCreateUserId, currentUserId)
                    .in(!StringUtils.equalsIgnoreCase(dto.getStampAttributes(), "2"), XjrBaseStamp::getStampAttributes, new Integer[]{0, 1})
                    .eq(StringUtils.equalsIgnoreCase(dto.getStampAttributes(), "2"), XjrBaseStamp::getStampAttributes, 2);
        }
        IPage<XjrBaseStamp> xjrBaseStampIPage = baseMapper.selectPage(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(xjrBaseStampIPage, StampVo.class);
    }

    @Override
    @Transactional
    public boolean setDefaultStamp(String id, String userId) {
        boolean flag = true;
        // 先查询是否有默认签章
        QueryWrapper<XjrBaseStamp> query = new QueryWrapper<>();
        query.eq("F_StampAttributes", "1").eq("F_CreateUserId", userId);
        XjrBaseStamp xjrBaseStamp = baseMapper.selectOne(query);
        if (xjrBaseStamp != null) {
            xjrBaseStamp.setStampAttributes(0);
            flag = baseMapper.updateById(xjrBaseStamp) > 0;
        }
        // 设置新的默认签章
        if (flag) {
            XjrBaseStamp oldXjrBaseStamp = getById(id);
            if (oldXjrBaseStamp != null) {
                oldXjrBaseStamp.setStampAttributes(1);
                flag = baseMapper.updateById(oldXjrBaseStamp) > 0;
            }
        }
        return flag;
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/12/24
    * @Param:[id, password]
    * @return:boolean
    * @Description:校验签章密码是否正确
    */
    @Override
    public boolean validatePwd(String id, String password) {
        XjrBaseStamp xjrBaseStamp = getById(id);
        if (xjrBaseStamp != null) {
            String stampPassword = xjrBaseStamp.getPassword();
            if (password!=null) {
                if (StringUtils.equals(password, stampPassword)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/12/24
    * @Param:[dto]
    * @return:boolean
    * @Description:保存签章
    */
    @Override
    public boolean saveStamp(SaveStampDto dto) {
        XjrBaseStamp xjrBaseStamp = BeanUtil.copy(dto, XjrBaseStamp.class);
        xjrBaseStamp.setStampId(IdWorker.getIdStr());
        xjrBaseStamp.setMaintain(StringPool.LEFT_SQ_BRACKET+ StringPool.QUOTE+SecureUtil.getUserId()+StringPool.QUOTE+StringPool.RIGHT_SQ_BRACKET);
        return baseMapper.insert(xjrBaseStamp) > 0;
    }

    @Override
    public boolean updateStamp(String id, SaveStampDto dto) {
            XjrBaseStamp xjrBaseStamp = BeanUtil.copy(dto, XjrBaseStamp.class);
            xjrBaseStamp.setStampId(id);
            return baseMapper.updateById(xjrBaseStamp) > 0;
    }

    @Override
    public boolean  addAuthorizeUser(String id, String authorizeUsers) {
        XjrBaseStamp xjrBaseStamp = getById(id);
        if (xjrBaseStamp != null) {
            xjrBaseStamp.setAuthorizeUser(authorizeUsers);
            return baseMapper.updateById(xjrBaseStamp) > 0;
        }
        return false;
    }



    /**
     * @Author:湘北智造-框架开发组
     * @Date:2020/11/6
     * @Param:[]
     * @return:void
     * @Description:判断当前用户是否具有维护权限
     */
    public Boolean checkMaintainUser(String id){
        boolean isMaintainUser= false;
        String currentUserId = SecureUtil.getUserId();
        XjrBaseStamp xjrBaseStamp = this.getById(id);
        if(xjrBaseStamp!=null&& StringUtil.isNotBlank(xjrBaseStamp.getMaintain())) {
            JSONArray objects = JSONArray.parseArray(xjrBaseStamp.getMaintain());
            // 判断权限
            for (Object object : objects) {
                String useId = object.toString();
                if (StringUtils.equals(useId,currentUserId)){
                    isMaintainUser = true;
                    break;
                }
            }
        }
        return isMaintainUser;
    }

    @Override
    public boolean addMember(String id, String member) {
        XjrBaseStamp xjrBaseStamp = getById(id);
        if (xjrBaseStamp != null) {
            xjrBaseStamp.setMember(member);
            return baseMapper.updateById(xjrBaseStamp) > 0;
        }
        return false;
    }

    @Override
    public boolean addMaintain(String id, String maintain) {
        XjrBaseStamp xjrBaseStamp = getById(id);
        if (xjrBaseStamp != null) {
            xjrBaseStamp.setMaintain(maintain);
            return baseMapper.updateById(xjrBaseStamp) > 0;
        }
        return false;
    }
}
