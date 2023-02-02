package com.xjrsoft.module.dingTalk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserGetDeptMemberRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.taobao.api.ApiException;
import com.xjrsoft.common.Enum.DeleteMarkEnum;
import com.xjrsoft.common.Enum.EnabledMarkEnum;
import com.xjrsoft.core.tool.utils.DigestUtil;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.entity.XjrBaseUserRelation;
import com.xjrsoft.module.base.service.IXjrBaseDepartmentService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.xjrsoft.module.dingTalk.DingTalkUtils.*;


@AllArgsConstructor
@RestController
@RequestMapping("/dingtalk")
@Api(value = "/dingtalk",tags = "钉钉相关")
public class DingTalkGetAllUser {

    protected static final Logger logger = LoggerFactory.getLogger(DingTalkGetAllUser.class);

    @Autowired
    private IXjrBaseDepartmentService iXjrBaseDepartmentService;
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptBaseResponselist = new ArrayList<>();
    @Autowired
    private IXjrBaseUserService iXjrBaseUserService;

    @Autowired
    private IXjrBaseUserRelationService iXjrBaseUserRelationService;
    /**
     * 获取企业下所有员工信息
     */
    @GetMapping("/getalluser")
    @ApiOperation(value="获取企业下所有员工信息")
    public  List getAllUser(String accessToken) {
        //钉钉中的用户信息
        List<OapiV2UserListResponse.ListUserResponse> userResponses = new ArrayList<>();
        //获取部门id
        OapiDepartmentListResponse resp = null;
        //获取token
        try {
            accessToken=getUserToken();
            System.out.println("#############"+accessToken);
            resp = getDeptList("1", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!resp.isSuccess()) {
            System.err.println(resp.getMessage());
        } else {

            String finalAccessToken = accessToken;
            resp.getDepartment().forEach(d -> {
                List <OapiV2UserListResponse.ListUserResponse> userlist = getDeptUser(finalAccessToken,Long.valueOf(d.getId()));
                System.out.println(Long.valueOf(d.getId())+"@@@"+userlist.size());
                userResponses.addAll(userlist);
            });
        }
        System.out.println(userResponses.size());
        //获取数据库中的用户信息
        List<XjrBaseUser> dbUsers = iXjrBaseUserService.list(Wrappers.<XjrBaseUser>query().lambda().eq(XjrBaseUser::getEnabledMark, EnabledMarkEnum.ENABLED.getCode()).eq(XjrBaseUser::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()));
        Map<String, XjrBaseUser> dbUsersMap = dbUsers.stream().collect(Collectors.toMap(v -> v.getMobile(), v -> v));
        Map<String, OapiV2UserListResponse.ListUserResponse> userResponsesMap =new HashMap<>();
        if(userResponses.size()>0) {
            userResponsesMap=userResponses.stream().collect(Collectors.toMap(v -> v.getMobile(), v -> v,(entity1, entity2) -> entity1));
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
                        .setEmail(userResponse.getEmail()).setModifyDate(LocalDateTimeUtil.now()).setDingTalkId(userResponse.getUserid());
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
                        .setEnabledMark(EnabledMarkEnum.ENABLED.getCode()).setDeleteMark(DeleteMarkEnum.NODELETE.getCode()).setCreateDate(LocalDateTimeUtil.now()).setDingTalkId(userResponse.getUserid());;
                iXjrBaseUserService.save(baseUser);
                XjrBaseUserRelation xjrBaseUserRelation = new XjrBaseUserRelation();
                xjrBaseUserRelation.setUserId(baseUser.getUserId()).setObjectId(userResponse.getDeptIdList().get(0).toString()).setCategory(3).setCreateDate(LocalDateTimeUtil.now());
                addRelations.add(xjrBaseUserRelation);
            }
        }
        iXjrBaseUserService.updateBatchById(updateBaseUsers);
        iXjrBaseUserRelationService.saveBatch(addRelations);
        return userResponses ;
    }

    @GetMapping("/getalluser22")
    public void getAllUser() throws ApiException {
        OapiDepartmentListResponse resp = getDeptList("1", true);
        List  userResponses = new ArrayList<>();

        if (!resp.isSuccess()) {
            System.err.println(resp.getMessage());
        } else {
            resp.getDepartment().forEach(d -> {
                System.out.println(d.getId() + "->" + d.getName());
                try {
                    List<String> usersId = getUsersIdByDeptId(d.getId().toString());
                    usersId.forEach(u -> {
                        System.out.println("---" + u);
                        userResponses.add(u);
                    });
                } catch (ApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
        System.out.println("@@@@@@"+userResponses.size());

    }
    /**
     * 根据父节点遍历所有的子节点
     * @from sanshu.cn
     * @param
     * @return
     * @throws ApiException
     */
    public OapiDepartmentListResponse getDeptList(String id,boolean  fetchChild) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setId(id);
        request.setHttpMethod("GET");
        request.setFetchChild(fetchChild);
        OapiDepartmentListResponse response = null;
        try {
            response = client.execute(request, getUserToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.getBody());
        return response;

    }

    /**
     * 根据获取用户编号
     * @from sanshu.cn
     * @param deptId 部门编号
     * @return
     * @throws ApiException
     */
    public List<String> getUsersIdByDeptId(String deptId) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
        OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
        req.setHttpMethod("GET");
        req.setDeptId(deptId);
        OapiUserGetDeptMemberResponse rsp = null;
        try {
            rsp = client.execute(req,getUserToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(rsp.getBody());
        return rsp.getUserIds();
    }

    @GetMapping("/getdept")
    @ApiOperation(value="获取企业下所有部门")
    public  OapiDepartmentListResponse getDept(String accessToken) {
        //获取部门id
        OapiDepartmentListResponse resp = null;
        //更新部门信息
        List<XjrBaseDepartment> baseDepartments = iXjrBaseDepartmentService.list(Wrappers.<XjrBaseDepartment>query().lambda().eq(XjrBaseDepartment::getCompanyId, "21db6a365d2f1254dac92f9ec314deaa").eq(XjrBaseDepartment::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()).eq(XjrBaseDepartment::getEnabledMark, EnabledMarkEnum.ENABLED.getCode()));
        ArrayList<XjrBaseDepartment> updateDeparts = new ArrayList<>();
        ArrayList<XjrBaseDepartment> addDeparts = new ArrayList<>();

        //获取token
        try {
            accessToken = getUserToken();
            System.out.println("#############" + accessToken);
            resp = getDeptList("1", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!resp.isSuccess()) {
            System.err.println(resp.getMessage());
        } else {
            resp.getDepartment().forEach(d -> {
                for (int i = 0; i < baseDepartments.size(); i++) {
                    XjrBaseDepartment baseDepartment = baseDepartments.get(i);
                    if (baseDepartment.getDepartmentId().equals(d.getId().toString())) {
                        baseDepartment.setFullName(d.getName()).setParentId(d.getParentid().equals(1L) ? "" : d.getParentid().toString());
                        updateDeparts.add(baseDepartment);
                    }
                }
                XjrBaseDepartment xjrBaseDepartment = new XjrBaseDepartment();
                xjrBaseDepartment.setDepartmentId(d.getId().toString()).setCompanyId("21db6a365d2f1254dac92f9ec314deaa").setDingTalkId(d.getId().toString())
                        .setFullName(d.getName()).setParentId(d.getParentid().equals(1L) ? "" : d.getParentid().toString())
                        .setDeleteMark(DeleteMarkEnum.NODELETE.getCode()).setEnabledMark(EnabledMarkEnum.ENABLED.getCode());
                addDeparts.add(xjrBaseDepartment);
            });

            iXjrBaseDepartmentService.saveBatch(addDeparts);
            iXjrBaseDepartmentService.updateBatchById(updateDeparts);
        }
        return resp ;
    }

    /**
     * 根据父节点遍历所有的子节点
     * @from sanshu.cn
     * @param
     * @return
     * @throws ApiException
     */
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDeptList2(String id,boolean  fetchChild) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setId(id);
        request.setHttpMethod("GET");
        request.setFetchChild(fetchChild);
        OapiDepartmentListResponse response = null;
        try {
            response = client.execute(request, getUserToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.getBody());
        return (List<OapiV2DepartmentListsubResponse.DeptBaseResponse>) response;

    }

}
