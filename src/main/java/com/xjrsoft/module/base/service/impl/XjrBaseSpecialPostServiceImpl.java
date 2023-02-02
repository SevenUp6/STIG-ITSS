package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseSpecialPost;
import com.xjrsoft.module.base.mapper.XjrBaseSpecialPostMapper;
import com.xjrsoft.module.base.service.IXjrBaseSpecialPostService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.vo.SpecialPostUserVo;
import com.xjrsoft.module.base.vo.SpecialPostVo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@Service
@AllArgsConstructor
public class XjrBaseSpecialPostServiceImpl extends ServiceImpl<XjrBaseSpecialPostMapper, XjrBaseSpecialPost> implements IXjrBaseSpecialPostService {

    private IXjrBaseUserRelationService userRelationService;
    
    @Autowired
    private XjrBaseSpecialPostMapper xjrBaseSpecialPostMapper;

    @Override
    public List<XjrBaseSpecialPost> getSpecialPostList(Integer type) {
        return this.list(Wrappers.<XjrBaseSpecialPost>query().lambda().eq(XjrBaseSpecialPost::getType, type));
    }

    @Override
    public boolean submit(List<XjrBaseSpecialPost> specialPostList, Integer type) {
        LambdaQueryWrapper<XjrBaseSpecialPost> queryWrapper = Wrappers.<XjrBaseSpecialPost>query().lambda().eq(XjrBaseSpecialPost::getType, type);
        //先删除后添加
        boolean flag = this.count(queryWrapper) == xjrBaseSpecialPostMapper.delete(queryWrapper);
        if(flag){
           this.saveBatch(specialPostList);
            return true;
        }
        return false;
    }

    public List<SpecialPostVo> getSpecialPost(String objectId, Integer objectType, Integer specialPostType) {
        List<SpecialPostUserVo> userList = userRelationService.getUsersOfSpecialPosts(objectId, objectType);
        List<XjrBaseSpecialPost> specialPostList = this.getSpecialPostList(specialPostType);
        List<SpecialPostVo> specialPostVoList = BeanUtil.copyList(specialPostList, SpecialPostVo.class);
        for (SpecialPostUserVo userVo: userList) {
            String specialPostId = StringUtils.substringBefore(userVo.getObjectId(), StringPool.UNDERSCORE);
            for (SpecialPostVo specialPostVo: specialPostVoList) {
                if (CollectionUtil.isEmpty(specialPostVo.getChildren())) {
                    specialPostVo.setChildren(new ArrayList<>());
                }
                if (StringUtil.equals(specialPostId, specialPostVo.getId())) {
                    specialPostVo.getChildren().add(userVo);
                    break;
                }
            }
        }
        return specialPostVoList;
    }
}
