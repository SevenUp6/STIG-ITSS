package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.page.PageInput;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.CodeRuleDto;
import com.xjrsoft.module.base.entity.XjrBaseCoderule;
import com.xjrsoft.module.base.service.IXjrBaseCodeRuleService;
import com.xjrsoft.module.base.vo.CodeRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 编号规则表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@AllArgsConstructor
@RestController
@RequestMapping("/code-rules")
@Api(value = "/code-rules",tags = "编码规则")
public class XjrBaseCodeRuleController {

    private IXjrBaseCodeRuleService codeRuleService;

    @ApiOperation(value="获取编码规则分页")
    @GetMapping
    public Response<PageOutput<CodeRuleVo>> getCodeRulePageList(PageInput dto){
        return Response.ok(codeRuleService.getCodeRulePageList(dto));
    }
    @ApiOperation(value="获取编码规则分页")
    @GetMapping("/list")
    public Response<List<CodeRuleVo>> getList(@RequestParam(required = false) String keyword){
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        List<XjrBaseCoderule> codeRuleList = codeRuleService.list(Wrappers.<XjrBaseCoderule>query().lambda()
                .eq(!StringUtil.isEmpty(keyword), XjrBaseCoderule::getFullName, keyword));
        return Response.ok(BeanUtil.copyList(codeRuleList, CodeRuleVo.class));
    }

    @GetMapping("/{id}")
    @ApiOperation(value="根据id 查询编码规则详情")
    @ApiImplicitParam(name = "编码规则id",value = "id",required = true,dataType = "string")
    public Response<XjrBaseCoderule> getCodeRuleById(@PathVariable String id){
        return Response.ok(codeRuleService.getCodeRuleById(id));
    }

    @PostMapping
    @ApiOperation(value="新增编码规则")
    public Response addCodeRule(@RequestBody CodeRuleDto codeRuleDto) {
        XjrBaseCoderule codeRule = BeanUtil.copy(codeRuleDto, XjrBaseCoderule.class);
        return Response.status(codeRuleService.save(codeRule));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改编码规则")
    @ApiImplicitParam(name = "编码规则id", value = "id", required = true, dataType = "string")
    public Response updateCodeRule(@PathVariable String id, @RequestBody CodeRuleDto codeRuleDto) {
        XjrBaseCoderule codeRule = BeanUtil.copy(codeRuleDto, XjrBaseCoderule.class);
        codeRule.setRuleId(id);
        return Response.status(codeRuleService.updateById(codeRule));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除编码规则")
    @ApiImplicitParam(name = "行政区域id,多个用逗号隔开",value = "id",required = true,dataType = "string")
    public Response deleteCodeRule(@PathVariable String ids){
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        if (idsArray.length > 1) {
            return Response.status(codeRuleService.removeByIds(Arrays.asList(idsArray)));
        } else {
            return Response.status(codeRuleService.removeById(ids));
        }
    }

    @GetMapping("/{encode}/gen")
    @ApiOperation(value="生成自动编码")
    @ApiImplicitParam(name = "自动编码编号",value = "encode",required = true,dataType = "string")
    public Response genEncode(@PathVariable String encode) {
        String code = codeRuleService.genEncode(encode);
        if (StringUtil.isEmpty(code)) {
            return Response.notOk();
        }
        return Response.ok(code);
    }

    @GetMapping("/{encode}/use")
    @ApiOperation(value="使用自动编码")
    @ApiImplicitParam(name = "自动编码编号",value = "encode",required = true,dataType = "string")
    public Response useEncode(@PathVariable String encode) {
        if (codeRuleService.useEncode(encode)) {
            return Response.ok();
        }
        return Response.notOk("没有编码可用，请生成！");
    }
}
