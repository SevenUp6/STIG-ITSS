package com.xjrsoft.module.base.mapper;

import com.xjrsoft.module.base.entity.XjrBasePost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.vo.PostTreeVo;

import java.util.List;

/**
 * <p>
 * 岗位表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-06
 */
public interface XjrBasePostMapper extends BaseMapper<XjrBasePost> {

    public List<PostTreeVo> getPostsOfCompany(String companyId, String departmentId, String keyword);
}
