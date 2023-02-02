package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.MemberUserIdDto;
import com.xjrsoft.module.base.dto.PostDto;
import com.xjrsoft.module.base.entity.XjrBasePost;
import com.xjrsoft.module.base.service.IXjrBasePostService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.MemberUserVo;
import com.xjrsoft.module.base.vo.UserVo;
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
 * 岗位表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
@Api(value = "/posts",tags = "岗位模块")
public class XjrBasePostController {

    private IXjrBasePostService postService;

    private IXjrBaseUserRelationService userRelationService;

    @PostMapping
    @ApiOperation(value="新增公司")
    public Response addPost(@RequestBody PostDto postDto) {
        XjrBasePost post = BeanUtil.copy(postDto, XjrBasePost.class);
        if (StringUtil.isBlank(post.getParentId())) {
            post.setParentId(StringPool.ZERO);
        }
        return Response.status(postService.save(post) && OrganizationCacheUtil.addCache(OrganizationCacheUtil.POST_LIST_CACHE_KEY, post));
    }

    @PutMapping("/{id}")
    @ApiOperation(value="修改公司")
    @ApiImplicitParam(name = "岗位id",value = "id", required = true, dataType = "string")
    public Response updatePost(@PathVariable String id, @RequestBody PostDto postDto) {
        XjrBasePost post = BeanUtil.copy(postDto, XjrBasePost.class);
        return Response.status(postService.updatePost(id, post));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value="删除公司")
    @ApiImplicitParam(name = "岗位id，多个用逗号隔开", value = "ids", required = true, dataType = "string")
    public Response deletePost(@PathVariable String ids){
        String[] idsArray = StringUtils.split(ids, StringPool.COMMA);
        boolean isSuccess = false;
        if (idsArray.length > 1) {
            isSuccess = postService.removeByIds(Arrays.asList(idsArray));
        } else {
            isSuccess = postService.removeById(ids);
        }
        if (isSuccess) {
            OrganizationCacheUtil.removeCaches(OrganizationCacheUtil.POST_LIST_CACHE_KEY, idsArray);
        }
        return Response.status(isSuccess);
    }

    @GetMapping("/{id}/users")
    @ApiOperation(value="获取岗位的人员")
    @ApiImplicitParam(name = "岗位id", value = "id", required = true, dataType = "string")
    public Response<List<MemberUserVo>> getUsersOfPost(@PathVariable String id, @RequestParam(name = "keyword", required = false) String keyword) {
        List<UserVo> userVoList = userRelationService.getMemberUserVoListOfObject(id, 2, keyword);
        List<MemberUserVo> memberUserVoList = BeanUtil.copyList(userVoList, MemberUserVo.class);
        return Response.ok(memberUserVoList, "查询成功！");
    }

    @PostMapping("/users")
    @ApiOperation(value="添加岗位的人员")
    public Response addUsersForPost(@RequestBody MemberUserIdDto memberUserIdDto) {
        String postId = memberUserIdDto.getPostId();
        List<String> userIds = memberUserIdDto.getUserIdList();
        return Response.status(userRelationService.addUserRelationsForObject(postId, 2, userIds));
    }
}
