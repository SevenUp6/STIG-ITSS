package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.mapper.XjrBaseDepartmentMapper;
import com.xjrsoft.module.base.service.IXjrBaseDepartmentService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.utils.TreeNodeUtil;
import com.xjrsoft.module.base.vo.DepartmentTreeVo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 * 部门信息表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
@Service
@AllArgsConstructor
public class XjrBaseDepartmentServiceImpl extends ServiceImpl<XjrBaseDepartmentMapper, XjrBaseDepartment> implements IXjrBaseDepartmentService {

    private IXjrBaseUserRelationService userRelationService;

    private IXjrBaseUserService userService;

    @Override
    public boolean addDepartment(XjrBaseDepartment department, Map<String, List<String>> postUserJson) {
        this.checkEnCode(department.getEnCode(), department.getDepartmentId());
        String departmentId = StringUtil.randomUUID();
        department.setDepartmentId(departmentId);
        if (StringUtil.isBlank(department.getParentId())) {
            department.setParentId(StringPool.ZERO);
        }
        userRelationService.saveSpecialPostRelations(postUserJson, departmentId, 5);
        return this.save(department) && OrganizationCacheUtil.addCache(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, department);
    }

    @Override
    public boolean updateDepartment(String departmentId, XjrBaseDepartment department, Map<String, List<String>> postUserJson) {
        this.checkEnCode(department.getEnCode(), department.getDepartmentId());
        department.setDepartmentId(departmentId);
        boolean isSavedSuccess = this.updateById(department) && OrganizationCacheUtil.updateCache(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY, department);
        userRelationService.removeSpecialPostRelations(departmentId, 5);
        userRelationService.saveSpecialPostRelations(postUserJson, departmentId,5);
        return isSavedSuccess;
    }

    @Override
    public String getDepartmentIdByLevel(String departmentId, Integer level) {
        for (int i = 0; i < level; i++) {
            XjrBaseDepartment xjrBaseDepartment = getById(departmentId);
            if (xjrBaseDepartment != null) {
                // 上一级的岗位id
                String parentDepartmentId = xjrBaseDepartment.getParentId();
                // 等于0是最高级公司
                if (StringUtils.equals(parentDepartmentId, "0")) {
                    departmentId = xjrBaseDepartment.getDepartmentId();
                    break;
                }else {
                    departmentId = parentDepartmentId;
                }
            }
        }
        return departmentId;
    }

    @Override
    public List<XjrBaseDepartment> getAllDepartmentList() {
        return this.list(Wrappers.<XjrBaseDepartment>query().lambda().eq(XjrBaseDepartment::getEnabledMark, 1));
    }

    @Override
    public List<String> getDepartmentAndChildrenIdList(List<String> departmentIdList) {
        List<XjrBaseDepartment> departmentList = this.getAllDepartmentList();
        List<DepartmentTreeVo> departmentTreeVoList = BeanUtil.copyList(departmentList, DepartmentTreeVo.class);
        ForestNodeMerger.merge(departmentTreeVoList);
        List<String> resultIdList = new ArrayList<>();
        for (DepartmentTreeVo departmentTreeVo : departmentTreeVoList) {
            if (departmentIdList.contains(departmentTreeVo.getDepartmentId())) {
                TreeNodeUtil.findChildrenId(departmentTreeVo, resultIdList);
                break;
            }
        }
        return resultIdList;
    }

    public void checkEnCode(String encode, String id) {
        if (this.count(Wrappers.<XjrBaseDepartment>query().lambda()
                .eq(XjrBaseDepartment::getEnCode, encode)
                .ne(!StringUtil.isEmpty(id), XjrBaseDepartment::getDepartmentId, id)) > 0) {
            throw new RuntimeException("编码重复：" + encode);
        }
    }

    @Override
    public boolean save(XjrBaseDepartment department) {
        this.checkEnCode(department.getEnCode(), department.getDepartmentId());
        return super.save(department);
    }

    @Override
    public boolean updateById(XjrBaseDepartment department) {
        this.checkEnCode(department.getEnCode(), department.getDepartmentId());
        return super.updateById(department);
    }

    //    /**
//     * 获取当前用户部门及下属部门id
//     * @return
//     */
//    public List<String> getDepartmentIdAndChildrenOfCurrentUser() {
//        List<XjrBaseDepartment> departmentList = userService.queryDepartmentsOfUser(SecureUtil.getUserId());
//        List<String> departmentIds = departmentList.stream().map(department -> department.getDepartmentId()).collect(Collectors.toList());
//        List<String> departmentAndChildrenIdList = this.getDepartmentAndChildrenIdList(departmentIds);
//        List<XjrBaseUserRelation> userRelationList = userRelationService.list(Wrappers.<XjrBaseUserRelation>query().lambda()
//                .eq(XjrBaseUserRelation::getCategory, 3).in(XjrBaseUserRelation::getObjectId, departmentAndChildrenIdList));
//        if (!CollectionUtil.isEmpty(userRelationList)) {
//            return userRelationList.stream().map(userRelation -> userRelation.getUserId()).collect(Collectors.toList());
//        }
//        return new ArrayList<>(0);
//    }
}
