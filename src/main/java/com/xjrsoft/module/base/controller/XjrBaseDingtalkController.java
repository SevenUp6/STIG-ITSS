package com.xjrsoft.module.base.controller;


import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.base.service.IXjrBaseDingtalkService;
import com.xjrsoft.module.base.service.IXjrBaseWeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/ding-talk")
@Api(value = "/ding-talk",tags = "钉钉信息更新")
public class XjrBaseDingtalkController {


//    @Autowired
//    private IXjrBaseWeChatService iXjrBaseWeChatService;


    @Autowired
    private IXjrBaseDingtalkService iXjrBaseDingtalkService;

    @PutMapping("/sync-user/{CompanyId}")
    @ApiOperation(value="钉钉信息更新")
   public Response updateInfo(@PathVariable("CompanyId") String CompanyId) throws Exception {
      return  Response.status(iXjrBaseDingtalkService.updateInfo(CompanyId));
   }

    @GetMapping()
    @ApiOperation(value="获取分页信息")
    public Response getList(@RequestParam(value = "keyword",required = false) String keyword,@RequestParam("limit") Integer limit,@RequestParam("size") Integer size,@RequestParam("companyId") String companyId){
//        return  Response.ok(iXjrBaseWeChatService.getList(keyword,limit,size,companyId));
        return Response.ok();
    }
}
