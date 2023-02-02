package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.AreaDto;
import com.xjrsoft.module.base.dto.CompanyDto;
import com.xjrsoft.module.base.entity.XjrBaseArea;
import com.xjrsoft.module.base.entity.XjrBaseCommonModule;
import com.xjrsoft.module.base.entity.XjrBaseCompany;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.service.IXjrBaseAreaService;
import com.xjrsoft.module.base.service.IXjrBaseCompanyService;
import com.xjrsoft.module.base.vo.AreaVo;
import com.xjrsoft.module.base.vo.CompanyTreeVo;
import com.xjrsoft.module.base.vo.DataItemDetailVo;
import com.xjrsoft.module.base.vo.SpecialPostVo;
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
 * 行政区域表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@AllArgsConstructor
@RestController
@RequestMapping("/areas")
@Api(value = "/areas",tags = "区域模块")
public class XjrBaseAreaController {

    private IXjrBaseAreaService areaService;

    @GetMapping("/province")
    @ApiOperation(value="获取所有省份")
    public Response<List<AreaVo>> getAllProvinceArea(@RequestParam(required = false) String keyword) {
        List<XjrBaseArea> provinceList = areaService.list(Wrappers.<XjrBaseArea>query().lambda().eq(XjrBaseArea::getParentId, "0").like(StringUtil.isNotBlank(keyword), XjrBaseArea::getAreaName, keyword));
        List<AreaVo> resultList = BeanUtil.copyList(provinceList, AreaVo.class);
        return Response.ok(resultList);
    }

    @GetMapping("/{id}/child")
    @ApiOperation(value="根据id 查询下级区域")
    @ApiImplicitParam(name = "上级id",value = "id",required = true,dataType = "string")
    public Response<List<AreaVo>> getAreaByParentId(@PathVariable String id, @ApiParam(value = "模糊查询关键字") @RequestParam(required = false) String keyword) {
        List<XjrBaseArea> provinceList = areaService.list(Wrappers.<XjrBaseArea>query().lambda().eq(XjrBaseArea::getParentId, id).like(StringUtil.isNotBlank(keyword), XjrBaseArea::getAreaName, keyword));
        List<AreaVo> resultList = BeanUtil.copyList(provinceList, AreaVo.class);
        return Response.ok(resultList);
    }

    @PostMapping
    @ApiOperation(value="新增行政区域")
    public Response addArea(@RequestBody AreaDto areaDto) {
        XjrBaseArea area = BeanUtil.copy(areaDto, XjrBaseArea.class);
        return Response.status(areaService.addArea(area));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改行政区域")
    @ApiImplicitParam(name = "行政区域id", value = "id", required = true, dataType = "string")
    public Response updateArea(@PathVariable String id, @RequestBody AreaDto areaDto) {
        XjrBaseArea area = BeanUtil.copy(areaDto, XjrBaseArea.class);
        return Response.status(areaService.updateArea(id, area));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除行政区域")
    @ApiImplicitParam(name = "行政区域id,多个用逗号隔开",value = "id",required = true,dataType = "string")
    public Response deleteArea(@PathVariable String ids){
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        if (idsArray.length > 1) {
            return Response.status(areaService.removeByIds(Arrays.asList(idsArray)));
        } else {
            return Response.status(areaService.removeById(ids));
        }
    }
}
