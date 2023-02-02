package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.vo.PostTreeVo;

import java.util.List;

/**
 * <p>
 * 岗位表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-06
 */
public interface IXjrBasePostService extends IService<XjrBasePost> {

    boolean updatePost(String postId, XjrBasePost post);

    /**
    * @Author:光华科技-软件研发部
    * @Date:2020/11/30
    * @Param:[postId, level]
    * @return:java.lang.String
    * @Description:根据level获取postId
    */
    String getPostIdByLevel(String postId, Integer level);

    List<PostTreeVo> getPostsOfCompany(String companyId, String departmentId, String keyword);

    List<String> getPostAndChildrenIdList(List<String> postIdList);

    void checkEnCode(String encode, String id);
}
