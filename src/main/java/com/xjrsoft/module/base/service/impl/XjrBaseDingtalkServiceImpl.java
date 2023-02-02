package com.xjrsoft.module.base.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.xjrsoft.common.Enum.DeleteMarkEnum;
import com.xjrsoft.common.Enum.EnabledMarkEnum;
import com.xjrsoft.core.tool.utils.DigestUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.WeChatDepartDto;
import com.xjrsoft.module.base.dto.WeChatUserDto;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.entity.XjrBaseUserRelation;
import com.xjrsoft.module.base.mapper.XjrBaseDepartmentMapper;
import com.xjrsoft.module.base.service.*;
//import com.xjrsoft.module.base.utils.DingtalkUtil;
import com.xjrsoft.module.base.utils.ListUtils;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.PageInfoVo;
import com.xjrsoft.module.base.vo.SynchronizationVo;
//import org.joda.time.LocalDate;
//import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 机构单位表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-20
 */
@Service
@Scope("prototype")
public class XjrBaseDingtalkServiceImpl implements IXjrBaseDingtalkService {

    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptBaseResponselist = new ArrayList<>();

    @Autowired
    private IXjrBaseDepartmentService iXjrBaseDepartmentService;

    @Autowired
    private IXjrBaseUserService iXjrBaseUserService;

    @Autowired
    private IXjrBaseUserRelationService iXjrBaseUserRelationService;

//    @Autowired
//    private DingtalkUtil dingtalkUtil;

    @Autowired
    private IXjrBaseCompanyService iXjrBaseCompanyService;



    @Override
    public Boolean updateInfo(String companyId) throws Exception {
        deptBaseResponselist.clear();
        this.getdept(1L);
        return this.updatedepart(companyId);
    }


    //更新部门信息
    private Boolean updatedepart(String companyId) throws Exception {
        //更新部门信息
        List<XjrBaseDepartment> baseDepartments = iXjrBaseDepartmentService.list(Wrappers.<XjrBaseDepartment>query().lambda().eq(XjrBaseDepartment::getCompanyId, companyId).eq(XjrBaseDepartment::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()).eq(XjrBaseDepartment::getEnabledMark, EnabledMarkEnum.ENABLED.getCode()));
        ArrayList<XjrBaseDepartment> updateDeparts = new ArrayList<>();
        ArrayList<XjrBaseDepartment> addDeparts = new ArrayList<>();

        out:for (OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse  : deptBaseResponselist) {
            for (int i = 0; i < baseDepartments.size(); i++) {
                XjrBaseDepartment baseDepartment = baseDepartments.get(i);
                if (baseDepartment.getDepartmentId().equals(deptBaseResponse.getDeptId().toString())) {
                    baseDepartment.setFullName(deptBaseResponse.getName()).setParentId(deptBaseResponse.getParentId().equals(1L) ? "" : deptBaseResponse.getParentId().toString());
                    updateDeparts.add(baseDepartment);
                    continue out;
                }
            }

            XjrBaseDepartment xjrBaseDepartment = new XjrBaseDepartment();
            xjrBaseDepartment.setDepartmentId(deptBaseResponse.getDeptId().toString()).setCompanyId(companyId).setDingTalkId(deptBaseResponse.getDeptId().toString())
                    .setFullName(deptBaseResponse.getName()).setParentId(deptBaseResponse.getParentId().equals(1L) ? "" : deptBaseResponse.getParentId().toString())
                    .setDeleteMark(DeleteMarkEnum.NODELETE.getCode()).setEnabledMark(EnabledMarkEnum.ENABLED.getCode());
            addDeparts.add(xjrBaseDepartment);
        }
        try {
            iXjrBaseDepartmentService.saveBatch(addDeparts);
            iXjrBaseDepartmentService.updateBatchById(updateDeparts);
        }catch (DuplicateKeyException e){
            throw  new Exception("用户和部门信息已更新到其他公司中!!!");
        }

        //更新用户信息
        List<OapiV2UserListResponse.ListUserResponse> userResponses = new ArrayList<>();
        //钉钉中的用户信息
//        for (OapiV2DepartmentListsubResponse.DeptBaseResponse DeptBaseResponse : deptBaseResponselist) {
//            userResponses.addAll(dingtalkUtil.getDepartmentUser(Long.valueOf(DeptBaseResponse.getDeptId())));
//        }

        for (int i = 0; i < userResponses.size(); i++) {
            if ("".equals(userResponses.get(i).getMobile())) {
                userResponses.remove(i);
            }
        }

        //获取数据库中的用户信息
        List<XjrBaseUser> dbUsers = iXjrBaseUserService.list(Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getEnabledMark,EnabledMarkEnum.ENABLED.getCode()).eq(XjrBaseUser::getDeleteMark,DeleteMarkEnum.NODELETE.getCode()));
        Map<String, XjrBaseUser> dbUsersMap = dbUsers.stream().collect(Collectors.toMap(v -> v.getMobile(), v -> v));
        Map<String, OapiV2UserListResponse.ListUserResponse> userResponsesMap =new HashMap<>();
        if(userResponses.size()>0) {
            userResponsesMap=userResponses.stream().collect(Collectors.toMap(v -> v.getMobile(), v -> v));
        }

        ArrayList<XjrBaseUserRelation> addRelations = new ArrayList<>();
        //更新用户
        ArrayList<XjrBaseUser> updateBaseUsers = new ArrayList<>();
        Set<String> distinctIds = CollUtil.intersectionDistinct(dbUsersMap.keySet(), userResponsesMap.keySet());
        if(distinctIds.size()>0) {
            for (String distinctId : distinctIds) {
                XjrBaseUser baseUser = dbUsersMap.get(distinctId);
                OapiV2UserListResponse.ListUserResponse userResponse = userResponsesMap.get(distinctId);
                baseUser.setRealName(userResponse.getName()).setHeadIcon(userResponse.getAvatar()).setEnCode(userResponse.getJobNumber())
                        .setEmail(userResponse.getEmail()).setCompanyId(companyId).setModifyDate(LocalDateTimeUtil.now()).setDingTalkId(userResponse.getUserid());
                updateBaseUsers.add(baseUser);
                List<XjrBaseUserRelation> list = iXjrBaseUserRelationService.list(Wrappers.<XjrBaseUserRelation>query().lambda().eq(XjrBaseUserRelation::getCategory, 3).eq(XjrBaseUserRelation::getUserId, baseUser.getUserId()).eq(XjrBaseUserRelation::getObjectId, userResponse.getDeptIdList().get(0)));
                if(list==null||list.size()<=0) {
                    XjrBaseUserRelation xjrBaseUserRelation = new XjrBaseUserRelation();
                    xjrBaseUserRelation.setUserId(baseUser.getUserId()).setObjectId( userResponse.getDeptIdList().get(0).toString()).setCategory(3).setCreateDate(LocalDateTimeUtil.now());
                    addRelations.add(xjrBaseUserRelation);
                }
            }
        }

        
        //添加数据库中没有的用户
        List<String> subtractIds2 = CollUtil.subtractToList(userResponsesMap.keySet(), dbUsersMap.keySet());
        if(subtractIds2.size()>0) {
            for (String subtractId : subtractIds2) {
                XjrBaseUser baseUser = new XjrBaseUser();
                OapiV2UserListResponse.ListUserResponse userResponse = userResponsesMap.get(subtractId);
                baseUser.setAccount(userResponse.getMobile()).setPassword(DigestUtil.encrypt("000000")).setRealName(userResponse.getName()).setHeadIcon(userResponse.getAvatar()).setEnCode(userResponse.getJobNumber()).setEmail(userResponse.getEmail()).setMobile(userResponse.getMobile())
                        .setCompanyId(companyId).setEnabledMark(EnabledMarkEnum.ENABLED.getCode()).setDeleteMark(DeleteMarkEnum.NODELETE.getCode()).setCreateDate(LocalDateTimeUtil.now()).setDingTalkId(userResponse.getUserid());;
                iXjrBaseUserService.save(baseUser);
                    XjrBaseUserRelation xjrBaseUserRelation = new XjrBaseUserRelation();
                    xjrBaseUserRelation.setUserId(baseUser.getUserId()).setObjectId(userResponse.getDeptIdList().get(0).toString()).setCategory(3).setCreateDate(LocalDateTimeUtil.now());
                    addRelations.add(xjrBaseUserRelation);
            }
        }
        iXjrBaseUserService.updateBatchById(updateBaseUsers);
        iXjrBaseUserRelationService.saveBatch(addRelations);
        return true;
    }

    //拉取钉钉中的所有部门id
    public void getdept(Long dept_id) throws Exception {
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> departmentList =null;// dingtalkUtil.getDepartmentList(dept_id);
        if (departmentList==null||departmentList.size() == 0) {
            return;
        }
        deptBaseResponselist.addAll(departmentList);
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse : departmentList) {
            getdept(deptBaseResponse.getDeptId());
        }
    }
}
