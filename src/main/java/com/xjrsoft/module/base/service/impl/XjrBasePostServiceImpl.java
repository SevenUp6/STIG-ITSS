package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.mapper.XjrBasePostMapper;
import com.xjrsoft.module.base.service.IXjrBasePostService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.utils.TreeNodeUtil;
import com.xjrsoft.module.base.vo.PostTreeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 岗位表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-06
 */
@Service
public class XjrBasePostServiceImpl extends ServiceImpl<XjrBasePostMapper, XjrBasePost> implements IXjrBasePostService {



    @Override
    public boolean updatePost(String postId, XjrBasePost post) {
        this.checkEnCode(post.getEnCode(), post.getPostId());
        post.setPostId(postId);
        return this.updateById(post) && OrganizationCacheUtil.updateCache(OrganizationCacheUtil.POST_LIST_CACHE_KEY, post);
    }


    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/30
    * @Param:[postId, level]
    * @return:java.lang.String
    * @Description:根据等级获取postId
    */
    public String getPostIdByLevel(String postId, Integer level) {
        for (int i = 0; i < level; i++) {
            XjrBasePost xjrBasePost = getById(postId);
            if (xjrBasePost != null) {
                // 上一级的岗位id
                String parentPostId = xjrBasePost.getParentId();
                if (StringUtils.equals(parentPostId, "0")) {
                    postId = xjrBasePost.getPostId();
                    break;
                }else {
                    postId = parentPostId;
                }
            }
        }
        return postId;
    }

    @Override
    public List<PostTreeVo> getPostsOfCompany(String companyId, String departmentId, String keyword) {
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        return this.baseMapper.getPostsOfCompany(companyId, departmentId, keyword);
    }

    public List<String> getPostAndChildrenIdList(List<String> postIdList) {
        List<XjrBasePost> postList = this.list();
        List<PostTreeVo> postTreeVoList = BeanUtil.copyList(postList, PostTreeVo.class);
        ForestNodeMerger.merge(postTreeVoList);
        List<String> resultIdList = new ArrayList<>();
        for (PostTreeVo postTreeVo : postTreeVoList) {
            if (postIdList.contains(postTreeVo.getPostId())) {
                TreeNodeUtil.findChildrenId(postTreeVo, resultIdList);
                break;
            }
        }
        return resultIdList;
    }

    public void checkEnCode(String encode, String id) {
        if (this.count(Wrappers.<XjrBasePost>query().lambda()
                .eq(XjrBasePost::getEnCode, encode)
                .ne(!StringUtil.isEmpty(id), XjrBasePost::getPostId, id)) > 0) {
            throw new RuntimeException("编码重复：" + encode);
        }
    }

    @Override
    public boolean save(XjrBasePost post) {
        this.checkEnCode(post.getEnCode(), post.getPostId());
        return super.save(post);
    }

    @Override
    public boolean updateById(XjrBasePost post) {
        this.checkEnCode(post.getEnCode(), post.getPostId());
        return super.updateById(post);
    }
}
