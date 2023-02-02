package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.node.ForestNodeMerger;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.DataItemDto;
import com.xjrsoft.module.base.entity.XjrBaseDataItem;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.service.IXjrBaseDataItemDetailService;
import com.xjrsoft.module.base.service.IXjrBaseDataItemService;
import com.xjrsoft.module.base.vo.DataItemDetailTreeVo;
import com.xjrsoft.module.base.vo.DataItemDetailVo;
import com.xjrsoft.module.base.vo.DataItemTreeVo;
import com.xjrsoft.module.base.vo.DataItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典分类表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/data-items")
@AllArgsConstructor
@Api(value = "/data-items",tags = "数据字典模块")
public class XjrBaseDataItemController {

    private IXjrBaseDataItemService itemService;

    private IXjrBaseDataItemDetailService itemDetailService;

    @GetMapping("{id}")
    @ApiOperation(value="获取数据字典分类详情")
    public Response<DataItemVo> getDataItemById(@PathVariable String id) {
        XjrBaseDataItem dataItem = itemService.getById(id);
        return Response.ok(BeanUtil.copy(dataItem, DataItemVo.class));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改数据字典分类")
    public Response updateDataItem(@PathVariable String id, @RequestBody DataItemDto dataItemDto) {
        if (!itemService.isUnique(dataItemDto.getItemCode(), id)) {
            return Response.notOk("编码已存在！");
        }
        XjrBaseDataItem dataItem = BeanUtil.copy(dataItemDto, XjrBaseDataItem.class);
        if (StringUtil.isEmpty(dataItem.getParentId())) {
            dataItem.setParentId(StringPool.ZERO);
        }
        dataItem.setItemId(id);
        return Response.status(itemService.updateById(dataItem));
    }

    @GetMapping("/{itemCode}/info")
    @ApiOperation(value="根据itemCode获取数据字典分类详情")
    public Response<DataItemVo> getDataItemByCode(@PathVariable String itemCode){
        XjrBaseDataItem dataItem = itemService.getOne(Wrappers.<XjrBaseDataItem>query().lambda().eq(XjrBaseDataItem::getItemCode, itemCode).eq(XjrBaseDataItem::getDeleteMark, 0));
        return Response.ok(BeanUtil.copy(dataItem, DataItemVo.class));
    }

    @GetMapping
    @ApiOperation(value="获取数据字典分类列表")
    public Response<List<DataItemTreeVo>> getDataItemList() {
        List<XjrBaseDataItem> dataItemList = itemService.list(Wrappers.<XjrBaseDataItem>query().lambda().eq(XjrBaseDataItem::getDeleteMark, 0));
        return Response.ok(BeanUtil.copyList(dataItemList, DataItemTreeVo.class));
    }

    @GetMapping("/tree")
    @ApiOperation(value="获取数据字典分类树列表")
    public Response<List<DataItemTreeVo>> getDataItemTreeList() {
        List<XjrBaseDataItem> dataItemList = itemService.list(Wrappers.<XjrBaseDataItem>query().lambda().eq(XjrBaseDataItem::getDeleteMark, 0));
        List<DataItemTreeVo> dataItemTreeVoList = BeanUtil.copyList(dataItemList, DataItemTreeVo.class);
        return Response.ok(ForestNodeMerger.merge(dataItemTreeVoList));
    }

    @PostMapping
    @ApiOperation(value="新增数据字典分类")
    public Response addDataItem(@RequestBody DataItemDto dataItemDto) {
        if (!itemService.isUnique(dataItemDto.getItemCode(), null)) {
            return Response.notOk("编码已存在！");
        }
        XjrBaseDataItem dataItem = BeanUtil.copy(dataItemDto, XjrBaseDataItem.class);
        if (StringUtil.isBlank(dataItem.getParentId())) {
            dataItem.setParentId(StringPool.ZERO);
        }
        return Response.status(itemService.save(dataItem));
    }

    @GetMapping("/{itemCode}/detail")
    @ApiOperation(value="根据itemCode查询字典分类详情列表")
    public Response<List<DataItemDetailVo>> getDataItemDetailListByCode(@PathVariable String itemCode, @RequestParam(name = "模糊查询关键词", required = false) String keyword){
        List<XjrBaseDataItemDetail> itemDetailList = itemDetailService.getDataItemDetailListByCode(itemCode, keyword);
        return Response.ok(BeanUtil.copyList(itemDetailList, DataItemDetailVo.class));
    }

    @GetMapping("/{itemCode}/detail/tree")
    @ApiOperation(value="根据itemCode查询字典分类详情树列表")
    public Response<List<DataItemDetailTreeVo>> getDataItemDetailTreeByCode(@PathVariable String itemCode, @RequestParam(name = "模糊查询关键词", required = false) String keyword){
        List<XjrBaseDataItemDetail> itemDetailList = itemDetailService.getDataItemDetailListByCode(itemCode, keyword);
        List<DataItemDetailTreeVo> dataItemTreeVoList = BeanUtil.copyList(itemDetailList, DataItemDetailTreeVo.class);
        return Response.ok(ForestNodeMerger.merge(dataItemTreeVoList));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除数据字典分类")
    public Response deleteDataItem(@PathVariable String ids) {
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idArray.length == 1) {
            isSuccess = itemService.removeById(ids);
        } else {
            isSuccess = itemService.removeByIds(Arrays.asList(idArray));
        }
        return Response.status(isSuccess);
    }
}
