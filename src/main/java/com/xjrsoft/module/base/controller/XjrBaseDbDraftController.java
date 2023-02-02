package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.dto.DbDraftDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDbDraft;
import com.xjrsoft.module.base.service.IXjrBaseDbDraftService;
import com.xjrsoft.module.base.vo.DbDraftVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 数据表草稿 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/database-draft")
@AllArgsConstructor
@Api(value = "/database-draft",tags = "数据库草稿模块")
public class XjrBaseDbDraftController {

    private IXjrBaseDbDraftService dbDraftService;

    @GetMapping("/{id}")
    @ApiOperation(value="获取数据库草稿表详情")
    public Response getDbDraftById(@PathVariable String id){
        XjrBaseDbDraft dbDraft = dbDraftService.getById(id);
        return Response.ok(BeanUtil.copy(dbDraft, DbDraftVo.class));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改数据库草稿表")
    public Response updateDbDraft(@PathVariable String id, DbDraftDto dbDraftDto){
        XjrBaseDbDraft dbDraft = BeanUtil.copy(dbDraftDto, XjrBaseDbDraft.class);
        dbDraft.setId(id);
        return Response.status(dbDraftService.updateById(dbDraft));
    }

    @GetMapping
    @ApiOperation(value="获取数据库草稿表列表数据, 分页")
    public Response getDbDraftPageList(GetPageListDto dto) {
        return Response.ok(dbDraftService.getDbDraftPageList(dto));
    }

    @PostMapping
    @ApiOperation(value="新增数据库草稿表")
    public Response addDbDraft(@RequestBody DbDraftDto dbDraftDto) {
        XjrBaseDbDraft dbDraft = BeanUtil.copy(dbDraftDto, XjrBaseDbDraft.class);
        return Response.status(dbDraftService.save(dbDraft));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除数据库草稿表")
    public Response deleteDbDraft(@PathVariable String ids) {
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idArray.length == 1) {
            isSuccess = dbDraftService.removeById(ids);
        } else {
            isSuccess = dbDraftService.removeByIds(Arrays.asList(idArray));
        }
        return Response.status(isSuccess);
    }
}
