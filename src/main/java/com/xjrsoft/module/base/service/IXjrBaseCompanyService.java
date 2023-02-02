package com.xjrsoft.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.vo.CompanyPageListVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构单位表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-10-20
 */
public interface IXjrBaseCompanyService extends IService<XjrBaseCompany> {
    XjrBaseCompany getCompanyById(String id);

    PageOutput<CompanyPageListVo> getCompanyPageList(GetPageListDto dto);

    boolean addCompany(XjrBaseCompany company, Map<String, List<String>> postUserJson);

    boolean updateCompany(String companyId, XjrBaseCompany company, Map<String, List<String>> postUserJson);

    String getCompanyIdByLevel(String companyId, Integer level);

    List<XjrBaseCompany> getCompanyList();

    List<String> getCompanyAndChildrenIdList(String companyId);

    /**
     * 判断encode是否重复
     * @param encode
     * @param id
     * @return
     */
    void checkEnCode(String encode, String id);
}
