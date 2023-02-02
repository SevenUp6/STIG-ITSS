package com.xjrsoft.module.form.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.xjrsoft.module.form.dto.FormRelationDto;
import com.xjrsoft.module.form.dto.FormRelationPageListDto;
import com.xjrsoft.module.form.dto.FormRelationSavedDto;
import com.xjrsoft.module.form.entity.XjrFormRelation;
import com.xjrsoft.module.form.service.IXjrFormRelationService;
import com.xjrsoft.module.form.vo.FormRelationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 表单-菜单关联关系表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/form-relation")
@Api(value = "/form-relation", tags = "表单管理模块")
public class XjrFormRelationController {

    private IXjrFormRelationService formRelationService;

    @GetMapping
    @ApiOperation(value="获取表单列表，分页")
    public Response<PageOutput<FormRelationVo>> getPageList(FormRelationPageListDto pageListDto) {
        return Response.ok(formRelationService.getPageList(pageListDto));
    }

    @PostMapping
    @ApiOperation(value = "新增表单管理")
    public Response<Boolean> addFormRelation(@RequestBody FormRelationSavedDto formRelationSavedDto) throws Exception {
        XjrFormRelation formRelation = BeanUtil.copy(formRelationSavedDto.getFormRelation(), XjrFormRelation.class);
        XjrBaseModule module = BeanUtil.copy(formRelationSavedDto.getModule(), XjrBaseModule.class);
        return Response.status(formRelationService.addFormRelation(formRelation, module));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改表单管理")
    public Response<Boolean> updateFormRelation(@PathVariable String id, @RequestBody FormRelationSavedDto formRelationSavedDto) throws Exception {
        XjrFormRelation formRelation = BeanUtil.copy(formRelationSavedDto.getFormRelation(), XjrFormRelation.class);
        XjrBaseModule module = BeanUtil.copy(formRelationSavedDto.getModule(), XjrBaseModule.class);
        return Response.status(formRelationService.updateFormRelation(id, formRelation, module));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除表单管理")
    public Response<Boolean> deleteFormRelation(@PathVariable String ids) {
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
//        boolean isSuccess = false;
//        if (idArray.length == 1) {
//            isSuccess = formRelationService.removeById(ids);
//        } else {
//            isSuccess = formRelationService.removeByIds(Arrays.asList(idArray));
//        }
        return Response.status(formRelationService.deleteFormRelation(idArray));
    }

    @GetMapping("/{moduleId}")
    @ApiOperation(value="根据菜单id查询表单管理")
    public Response<FormRelationVo> getFormRelationById(@PathVariable String moduleId) {
        XjrFormRelation formRelation = formRelationService.getOne(Wrappers.<XjrFormRelation>query().lambda().eq(XjrFormRelation::getModuleId, moduleId));
        return Response.ok(BeanUtil.copy(formRelation, FormRelationVo.class));
    }
}
