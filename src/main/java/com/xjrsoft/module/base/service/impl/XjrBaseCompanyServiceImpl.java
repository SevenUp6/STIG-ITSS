package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.mapper.XjrBaseCompanyMapper;
import com.xjrsoft.module.base.service.IXjrBaseCompanyService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.utils.TreeNodeUtil;
import com.xjrsoft.module.base.vo.CompanyPageListVo;
import com.xjrsoft.module.base.vo.CompanyTreeVo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构单位表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-10-20
 */
@Service
@AllArgsConstructor
public class XjrBaseCompanyServiceImpl extends ServiceImpl<XjrBaseCompanyMapper, XjrBaseCompany> implements IXjrBaseCompanyService {

    private IXjrBaseUserRelationService userRelationService;

    @Override
    public XjrBaseCompany getCompanyById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public PageOutput<CompanyPageListVo> getCompanyPageList(GetPageListDto dto) {
        QueryWrapper<XjrBaseCompany> query = new QueryWrapper<>();
        query.select(XjrBaseCompany.class,x -> VoToColumn.Convert(CompanyPageListVo.class).contains(x.getColumn()));
        query.lambda().like(!StrUtil.hasBlank(dto.getKeyword()),XjrBaseCompany::getFullName,dto.getKeyword());

        IPage<XjrBaseCompany> xjrBaseCompanyIPage = baseMapper.selectPage(ConventPage.getPage(dto), query);

        return ConventPage.getPageOutput(xjrBaseCompanyIPage, CompanyPageListVo.class);
    }

    @Override
    public boolean addCompany(XjrBaseCompany company, Map<String, List<String>> postUserJson){
        this.checkEnCode(company.getEnCode(), company.getCompanyId());
        String companyId = StringUtil.randomUUID();
        company.setCompanyId(companyId);
        if (StringUtil.isBlank(company.getParentId())) {
            company.setParentId(StringPool.ZERO);
        }
        userRelationService.saveSpecialPostRelations(postUserJson, companyId, 4);
        return this.save(company) && OrganizationCacheUtil.addCache(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, company);
    }

    @Override
    public boolean updateCompany(String companyId, XjrBaseCompany company, Map<String, List<String>> postUserJson) {
        this.checkEnCode(company.getEnCode(), company.getCompanyId());
        company.setCompanyId(companyId);
        boolean isSavedSuccess = this.updateById(company) && OrganizationCacheUtil.updateCache(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, company);
        userRelationService.removeSpecialPostRelations(companyId, 4);
        userRelationService.saveSpecialPostRelations(postUserJson, companyId,4);
        return isSavedSuccess;
    }

    @Override
    public String getCompanyIdByLevel(String companyId, Integer level) {
        for (int i = 0; i < level; i++) {
            XjrBaseCompany xjrBaseCompany = getCompanyById(companyId);
            if (xjrBaseCompany != null) {
                // 上一级的岗位id
                String parentPostId = xjrBaseCompany.getParentId();
                // 等于0是最高级公司
                if (StringUtils.equals(parentPostId, "0")) {
                    companyId = xjrBaseCompany.getCompanyId();
                    break;
                }else {
                    companyId = parentPostId;
                }
            }
        }
        return companyId;
    }

    @Override
    public List<XjrBaseCompany> getCompanyList() {
        return this.list(Wrappers.<XjrBaseCompany>query().lambda().eq(XjrBaseCompany::getEnabledMark, 1));
    }

    public List<String> getCompanyAndChildrenIdList(String companyId) {
        List<XjrBaseCompany> companyList = this.getCompanyList();
        List<CompanyTreeVo> companyTreeVoList = BeanUtil.copyList(companyList, CompanyTreeVo.class);
        ForestNodeMerger.merge(companyTreeVoList);
        List<String> resultIdList = new ArrayList<>();
        for (CompanyTreeVo companyTreeVo : companyTreeVoList) {
            if (StringUtil.equals(companyTreeVo.getCompanyId(), companyId)) {
                TreeNodeUtil.findChildrenId(companyTreeVo, resultIdList);
                break;
            }
        }
        return resultIdList;
    }

    /**
     * 检查编码是否重复
     * @param encode
     * @param id
     */
    public void checkEnCode(String encode, String id) {
        if (this.count(Wrappers.<XjrBaseCompany>query().lambda()
                .eq(XjrBaseCompany::getEnCode, encode)
                .ne(!StringUtil.isEmpty(id), XjrBaseCompany::getCompanyId, id)) > 0) {
            throw new RuntimeException("编码重复：" + encode);
        }
    }

    @Override
    public boolean save(XjrBaseCompany entity) {
        this.checkEnCode(entity.getEnCode(), entity.getCompanyId());
        return super.save(entity);
    }

    @Override
    public boolean updateById(XjrBaseCompany entity) {
        this.checkEnCode(entity.getEnCode(), entity.getCompanyId());
        return super.updateById(entity);
    }
}
