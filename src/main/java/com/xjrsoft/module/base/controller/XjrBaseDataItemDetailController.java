package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.Enum.DeleteMarkEnum;
import com.xjrsoft.common.Enum.EnabledMarkEnum;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.DataItemDetailDto;
import com.xjrsoft.module.base.entity.XjrBaseDataItem;
import com.xjrsoft.module.base.entity.XjrBaseDataItemDetail;
import com.xjrsoft.module.base.service.IXjrBaseDataItemDetailService;
import com.xjrsoft.module.base.service.IXjrBaseDataItemService;
import com.xjrsoft.module.base.utils.PinYinUtil;
import com.xjrsoft.module.base.vo.DataItemDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 数据字典明细表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/data-item-detail")
@AllArgsConstructor
@Api(value = "/data-item-detail", tags = "数据字典详情模块")
public class XjrBaseDataItemDetailController {

    private IXjrBaseDataItemDetailService itemDetailService;

    private IXjrBaseDataItemService itemService;

    @GetMapping("/{id}")
    @ApiOperation(value="根据id获取数据字典详情")
    public Response getItemDetailById(@PathVariable String id){
        XjrBaseDataItemDetail itemDetail = itemDetailService.getById(id);
        return Response.ok(BeanUtil.copy(itemDetail, DataItemDetailVo.class));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改数据字典详情")
    public Response updateItemDetail(@PathVariable String id, @RequestBody DataItemDetailDto itemDetailDto) {
        XjrBaseDataItemDetail itemDetail = BeanUtil.copy(itemDetailDto, XjrBaseDataItemDetail.class);
        itemDetail.setItemDetailId(id);
        itemDetail.setSimpleSpelling(PinYinUtil.getPinYinHeaderChar(itemDetail.getItemName()));
        return Response.status(itemDetailService.updateById(itemDetail));
    }

    @GetMapping
    @ApiOperation(value="获取数据字典 列表")
    public Response getItemDetailList() {
        List<XjrBaseDataItemDetail> itemDetailList = itemDetailService.list(Wrappers.<XjrBaseDataItemDetail>query().lambda().eq(XjrBaseDataItemDetail::getDeleteMark, 0));
        return Response.ok(BeanUtil.copyList(itemDetailList, XjrBaseDataItemDetail.class));
    }

    @PostMapping
    @ApiOperation(value="新增数据字典详情")
    public Response addItemDetail(@RequestBody DataItemDetailDto itemDetailDto) {
        //添加类别
        if(!StringUtil.isEmpty(itemDetailDto.getItemCode())) {
            XjrBaseDataItem xjrBaseDataItem = itemService.getOne(Wrappers.<XjrBaseDataItem>query().lambda().eq(XjrBaseDataItem::getItemCode, itemDetailDto.getItemCode()).eq(XjrBaseDataItem::getDeleteMark, DeleteMarkEnum.NODELETE.getCode()).eq(XjrBaseDataItem::getEnabledMark, EnabledMarkEnum.ENABLED.getCode()));
            itemDetailDto.setItemId(xjrBaseDataItem.getItemId());
        }
        XjrBaseDataItemDetail itemDetail = BeanUtil.copy(itemDetailDto, XjrBaseDataItemDetail.class);
        itemDetail.setSimpleSpelling(PinYinUtil.getPinYinHeaderChar(itemDetail.getItemName()));
        if (StringUtil.isBlank(itemDetail.getParentId())) {
            itemDetail.setParentId(StringPool.ZERO);
        }
        return Response.status(itemDetailService.save(itemDetail));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除数据字典详情")
    public Response deleteItemDetail(@PathVariable String ids) {
        String[] idArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idArray.length == 1) {
            isSuccess = itemDetailService.removeById(ids);
        } else {
            isSuccess = itemDetailService.removeByIds(Arrays.asList(idArray));
        }
        return Response.status(isSuccess);
    }
}
