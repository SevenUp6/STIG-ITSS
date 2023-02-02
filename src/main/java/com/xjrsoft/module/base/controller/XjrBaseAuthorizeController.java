package com.xjrsoft.module.base.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.base.dto.AuthorizeIdsDto;
import com.xjrsoft.module.base.dto.SaveAuthorizeDto;
import com.xjrsoft.module.base.entity.XjrBaseAuthorize;
import com.xjrsoft.module.base.service.IXjrBaseAuthorizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>
 * 授权功能表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("authorizes")
@Api(value = "/authorizes",tags = "功能授权模块")
public class XjrBaseAuthorizeController {

    private IXjrBaseAuthorizeService authorizeService;

    @GetMapping("/{objectType}/{objectId}")
    @ApiOperation(value="获取角色、用户的菜单权限")
    public Response getAuthorizesOfObject(@PathVariable String objectId, @PathVariable String objectType) {
        List<XjrBaseAuthorize> authorizeList = authorizeService.list(Wrappers.<XjrBaseAuthorize>query().lambda().eq(XjrBaseAuthorize::getObjectId, objectId).eq(XjrBaseAuthorize::getObjectType, objectType));
        LinkedHashSet[] resultList = new LinkedHashSet[]{new LinkedHashSet(), new LinkedHashSet(), new LinkedHashSet(), new LinkedHashSet()};
        for (XjrBaseAuthorize authorize : authorizeList) {
            Integer itemType = authorize.getItemType();
            String itemId = authorize.getItemId();
            switch (itemType) {
                case 1 :
                    resultList[0].add(itemId);
                    break;
                case 2:
                    resultList[1].add(itemId);
                    break;
                case 3:
                    resultList[2].add(itemId);
                    break;
                case 4:
                    resultList[3].add(itemId);
                    break;
            }
        }
        return Response.ok(resultList);
    }

    @PostMapping
    @ApiOperation(value="功能菜单的授权")
    public Response authorize(@RequestBody SaveAuthorizeDto saveAuthorizeDto) {
        String objectId = saveAuthorizeDto.getObjectId();
        Integer objectType = saveAuthorizeDto.getObjectType();
        AuthorizeIdsDto authorizeIdsDto = saveAuthorizeDto.getAuthorizeIdsDto();
        return Response.status(authorizeService.submit(objectId, objectType, authorizeIdsDto));
    }
}
