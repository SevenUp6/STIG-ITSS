package com.xjrsoft.module.language.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.base.service.IXjrBaseDataItemDetailService;
import com.xjrsoft.module.language.dto.GetListLgMapDto;
import com.xjrsoft.module.language.dto.LgMapDto;
import com.xjrsoft.module.language.entity.XjrLgMap;
import com.xjrsoft.module.language.service.IXjrLgMapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 语言映照表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/lp-map")
@Api(value = "/lg-type", tags = "翻译模块")
public class XjrLgMapController {

    private IXjrLgMapService lgMapService;

    private IXjrBaseDataItemDetailService detaItemService;

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:获取详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取详情")
    public Response getLgMapById(@PathVariable String id) {
        return Response.ok(lgMapService.getById(id));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:修改
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    public Response update(@PathVariable String id, @RequestBody LgMapDto dto) {
        dto.setId(id);
        return Response.status(lgMapService.updateById(BeanUtil.copy(dto, XjrLgMap.class)));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[objectId]
     * @return:com.xjrsoft.common.result.Response
     * @Description:根据 F_ItemId / F_ModuleId 查询
     */
    @GetMapping("/object/{objectId}")
    @ApiOperation(value = "根据 F_ItemId / F_ModuleId 查询")
    public Response getLgByObjectId(@PathVariable String objectId, @RequestParam(value = "keyword", required = false) String keyword) {
        return Response.ok(lgMapService.getByCode(objectId, keyword));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[objectId, keyword]
     * @return:com.xjrsoft.common.result.Response
     * @Description:根据 F_ItemId / F_ModuleId 删除翻译
     */
    @DeleteMapping("/object/{objectId}")
    @ApiOperation(value = "根据 F_ItemId / F_ModuleId 删除翻译")
    public Response delLgByObjectId(@PathVariable String objectId) {
        QueryWrapper<XjrLgMap> query = new QueryWrapper<XjrLgMap>().eq("F_Code", objectId);
        return Response.ok(lgMapService.remove(query));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[objectId, keyword]
     * @return:com.xjrsoft.common.result.Response
     * @Description:根据 F_ItemId / F_ModuleId 新增翻译
     */
    @PostMapping("/object")
    @ApiOperation(value = "根据 F_ItemId / F_ModuleId 新增翻译")
    public Response saveLgByObjectId(@RequestBody Map<String, Object> params) {
        return Response.status(lgMapService.saveLgByObjectId(params));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[objectId, keyword]
     * @return:com.xjrsoft.common.result.Response
     * @Description:根据 F_ItemId / F_ModuleId 更新翻译
     */
    @SneakyThrows
    @PatchMapping("/object")
    @ApiOperation(value = "根据 F_ItemId / F_ModuleId 更新翻译")
    public Response updateLgByObjectId(@RequestBody Map<String, Object> params) {
        return Response.ok(lgMapService.updateLgByObjectId(params));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[objectId, keyword]
     * @return:com.xjrsoft.common.result.Response
     * @Description:
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取列表 分页")
    public Response getPageData(GetListLgMapDto dto) {
        return Response.ok(lgMapService.getPageData(dto));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/12
     * @Param:[ids]
     * @return:com.xjrsoft.common.result.Response
     * @Description:删除
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除")
    public Response getPageData(@PathVariable String ids) {
        return Response.status(lgMapService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
