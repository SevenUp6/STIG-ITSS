package com.xjrsoft.module.base.service;

import com.xjrsoft.module.base.entity.XjrBaseSpecialPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.vo.SpecialPostVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
public interface IXjrBaseSpecialPostService extends IService<XjrBaseSpecialPost> {

    /**
     *
     * @param type 1-公司，2-部门
     * @return
     */
    List<XjrBaseSpecialPost> getSpecialPostList(Integer type);

    /**
     *
     * @param specialPostList
     * @param type 1-公司，2-部门
     * @return
     */
    boolean submit(List<XjrBaseSpecialPost> specialPostList, Integer type);

    /**
     *
     * @param objectId
     * @param objectType 4-公司， 5-部门
     * @param specialPostType 1-公司， 2- 部门
     * @return
     */
    List<SpecialPostVo> getSpecialPost(String objectId, Integer objectType, Integer specialPostType);
}
