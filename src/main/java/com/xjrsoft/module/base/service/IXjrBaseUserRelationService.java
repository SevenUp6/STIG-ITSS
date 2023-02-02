package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.entity.XjrBaseUserRelation;
import com.xjrsoft.module.base.vo.SpecialPostUserVo;
import com.xjrsoft.module.base.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关系表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-04
 */
public interface IXjrBaseUserRelationService extends IService<XjrBaseUserRelation> {

    /**
     *
     * @param postUserJson
     * @param
     * @return
     */
    boolean saveSpecialPostRelations(Map<String, List<String>> postUserJson, String objectId, Integer objectType);

    /**
     *
     * @param objectId
     * @param objectType  4-公司，5-部门
     * @return
     */
    List<SpecialPostUserVo> getUsersOfSpecialPosts(String objectId, Integer objectType);

    /**
     *
     * @param objectId
     * @param objectType 4-公司岗位，5-部门岗位
     * @return
     */
    boolean removeSpecialPostRelations(String objectId, Integer objectType);

    /**
     *
     * @param objectId
     * @param objectType 1-角色， 2- 岗位， 3 部门
     * @param userIdList
     * @return
     */
    boolean addUserRelationsForObject(String objectId, Integer objectType, List<String> userIdList);

    /**
     *
     * @param departmentId
     * @param objectType 1-角色， 2- 岗位， 3 部门
     * @return
     */
    List<XjrBaseUser> getUsersOfObject(String departmentId, Integer objectType);

    /**
     *
     * @param departmentId
     * @param objectType 1-角色， 2- 岗位， 3 部门
     * @param keyword
     * @return
     */
    List<UserVo> getMemberUserVoListOfObject(String departmentId, Integer objectType, String keyword);

    /**
     *
     * @param userId
     * @param objectType 1-角色， 2- 岗位， 3 部门
     * @param objectIdList
     * @param isRemovingExits 是否删除改人员已有 角色/部门/岗位
     * @return
     */
    boolean addUserRelationsForUser(String userId, Integer objectType, List<String> objectIdList, boolean isRemovingExits);

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/12/1
    * @Param:[userId]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseUserRelation>
    * @Description:获取用户的 1-角色， 2- 岗位， 3 部门
    */
    List<XjrBaseUserRelation> getObjectsOfUser(String userId, Integer objectType);
}
