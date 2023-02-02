package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门信息表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-26
 */
public interface IXjrBaseDepartmentService extends IService<XjrBaseDepartment> {

    boolean addDepartment(XjrBaseDepartment department, Map<String, List<String>> postUserJson);

    boolean updateDepartment(String departmentId, XjrBaseDepartment department, Map<String, List<String>> postUserJson);

    String getDepartmentIdByLevel(String departmentId, Integer level);

    List<XjrBaseDepartment> getAllDepartmentList();

    List<String> getDepartmentAndChildrenIdList(List<String> departmentIdList);

    void checkEnCode(String encode, String id);
}
