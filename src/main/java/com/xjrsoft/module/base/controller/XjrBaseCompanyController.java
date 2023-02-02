package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.CompanyDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDepartment;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 机构单位表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-20
 */
@AllArgsConstructor
@RestController
@RequestMapping("/companies")
@Api(value = "/companies", tags = "公司模块")
public class XjrBaseCompanyController {

    private IXjrBasePostService postService;

    private IXjrBaseDepartmentService departmentService;

    private IXjrBaseCompanyService companyService;

    private IXjrBaseUserRelationService userRelationService;

    private IXjrBaseSpecialPostService specialPostService;


    @GetMapping("/{id}")
    @ApiOperation(value="获取公司详情")
    @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "string")
    public Response<XjrBaseCompany> getById(@PathVariable String id){
        return Response.ok(companyService.getCompanyById(id));
    }

    @ApiOperation(value="获取公司列表分页")
    @GetMapping("/page")
    public Response<PageOutput<CompanyPageListVo>> getCompanyPageList(GetPageListDto dto){
        return Response.ok(companyService.getCompanyPageList(dto));
    }

    @ApiOperation(value="获取公司树")
    @GetMapping("/tree")
    public Response<List<CompanyTreeVo>> getCompanyTree(@ApiParam(value = "模糊查询关键字") @RequestParam(required = false) String keyword) {
        List<XjrBaseCompany> companyList = companyService.list(Wrappers.<XjrBaseCompany>query().lambda().eq(XjrBaseCompany::getEnabledMark, 1).eq(XjrBaseCompany::getDeleteMark, 0).like(StringUtil.isNotBlank(keyword), XjrBaseCompany::getFullName, keyword).orderByAsc(XjrBaseCompany::getSortCode));
        List<CompanyTreeVo> companyVoList = BeanUtil.copyList(companyList, CompanyTreeVo.class);
        return Response.ok(ForestNodeMerger.merge(companyVoList));
    }

    @ApiOperation(value="获取公司下的部门树")
    @GetMapping("/{id}/departments/tree")
    @ApiImplicitParam(name = "公司id",value = "id",required = true,dataType = "string")
    public Response<List<DepartmentTreeVo>> getDepartmentTreeOfCom(@PathVariable String id, @ApiParam(value = "模糊查询关键字") @RequestParam(required = false) String keyword) {
        List<XjrBaseDepartment> depaList = departmentService.list(Wrappers.<XjrBaseDepartment>query().lambda().eq(XjrBaseDepartment::getCompanyId, id).like(StringUtil.isNotBlank(keyword), XjrBaseDepartment::getFullName, keyword));
        List<DepartmentTreeVo> depaVoList = BeanUtil.copyList(depaList, DepartmentTreeVo.class);
        return Response.ok(ForestNodeMerger.merge(depaVoList));
    }

    @PostMapping
    @ApiOperation(value="新增公司")
    public Response addCompany(@RequestBody CompanyDto companyDto) {
        Map<String, List<String>> postUserJson = companyDto.getPostUserJson();
        XjrBaseCompany company = BeanUtil.copy(companyDto, XjrBaseCompany.class);
        return Response.status(companyService.addCompany(company, postUserJson));
    }

    @GetMapping("/{id}/special-post")
    @ApiOperation(value="查询公司特殊岗位")
    @ApiImplicitParam(name = "公司id",value = "id", required = true, dataType = "string")
    public Response<List<SpecialPostVo> > getSpecialPost(@PathVariable String id) {
        List<SpecialPostVo> specialPostVoList = specialPostService.getSpecialPost(id, 4, 1);
        return Response.ok(specialPostVoList, "查询成功！");
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除公司")
    @ApiImplicitParam(name = "公司id,多个用逗号隔开",value = "id",required = true,dataType = "string")
    public Response deleteCompany(@PathVariable String ids){
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idsArray.length > 1) {
            isSuccess = companyService.removeByIds(Arrays.asList(idsArray));
        } else {
            isSuccess = companyService.removeById(ids);
        }
        // 更新缓存
        if (isSuccess) {
            OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY, idsArray);
        }
        return Response.status(isSuccess);
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改公司信息")
    @ApiImplicitParam(name = "公司id", value = "id", required = true, dataType = "string")
    public Response updateCompany(@PathVariable String id, @RequestBody CompanyDto companyDto) {
        Map<String, List<String>> postUserJson = companyDto.getPostUserJson();
        XjrBaseCompany company = BeanUtil.copy(companyDto, XjrBaseCompany.class);
        Integer count = companyService.count(Wrappers.<XjrBaseCompany>query().lambda().eq(XjrBaseCompany::getCompanyId, id));
        if (count > 0) {
            return Response.status(companyService.updateCompany(id, company, postUserJson));
        }
        return Response.notOk("该公司不存在！");
    }

    @GetMapping("/{id}/posts/tree")
    @ApiOperation(value="获取公司下的所有 岗位 树")
    @ApiImplicitParam(name = "公司id",value = "id",required = true,dataType = "string")
    public Response<List<PostTreeVo>> getPostsOfCompany(@PathVariable String id,
                                                        @ApiParam(value = "部门id", required = false) @RequestParam(name = "Department_Id", required =  false) String departmentId,
                                                        @ApiParam(value = "模糊查询关键字") @RequestParam(name = "keyword", required =  false) String keyword) {
        List<PostTreeVo> postTreeVoList = postService.getPostsOfCompany(id, departmentId, keyword);
        return Response.ok(ForestNodeMerger.merge(postTreeVoList), "查询成功！");
    }

    @GetMapping("/departments/tree")
    @ApiOperation(value="获取所有公司 部门树")
    public Response<List<CompanyTreeVo>> getDepartmentsTreeOfAll() {
        List<XjrBaseCompany> companyList = companyService.list(Wrappers.<XjrBaseCompany>query().lambda().eq(XjrBaseCompany::getEnabledMark, 1));
        List<String> companyIdList = companyList.stream().map(company -> company.getCompanyId()).collect(Collectors.toList());
        List<XjrBaseDepartment> departmentList = departmentService.list(Wrappers.<XjrBaseDepartment>query().lambda().in(CollectionUtil.isNotEmpty(companyIdList), XjrBaseDepartment::getCompanyId, companyIdList).eq(XjrBaseDepartment::getEnabledMark, 1).orderByDesc(XjrBaseDepartment::getCreateDate));
        List<DepartmentTreeVo> departmentTree = ForestNodeMerger.merge(BeanUtil.copyList(departmentList, DepartmentTreeVo.class));
        List<CompanyTreeVo> companyTreeVoList = BeanUtil.copyList(companyList, CompanyTreeVo.class);
        List<CompanyTreeVo> companyTree = ForestNodeMerger.merge(companyTreeVoList);
        for (CompanyTreeVo companyVo: companyTreeVoList) {
            for (DepartmentTreeVo department : departmentTree) {
                if (StringUtil.equals(companyVo.getCompanyId(), department.getCompanyId())) {
                    companyVo.getChildren().add(department);
                }
            }
        }
        return Response.ok(companyTree);
    }
}
