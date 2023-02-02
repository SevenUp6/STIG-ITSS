package com.xjrsoft.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.entity.XjrBaseUserRelation;
import com.xjrsoft.module.base.vo.SpecialPostUserVo;
import com.xjrsoft.module.base.vo.UserVo;

import java.util.List;

/**
 * <p>
 * 用户关系表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-11-04
 */
public interface XjrBaseUserRelationMapper extends BaseMapper<XjrBaseUserRelation> {

    List<SpecialPostUserVo> getUsersOfSpecialPosts(String objectId, Integer objectType);

    List<XjrBaseUser> getUsersOfObject(String objectId, Integer objectType);

    List<UserVo> getMemberUserVoListOfObject(String objectId, Integer objectType, String keyword);
}
