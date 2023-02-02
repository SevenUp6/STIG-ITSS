package com.xjrsoft.module.dingTalk;

import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.xjrsoft.core.tool.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/dingtalk")
@Api(value = "/dingtalk",tags = "钉钉相关")
public class DingTalkGetAccessToken {
    @GetMapping("/getaccesstoken")
    @ApiOperation(value = "获取token")
    public static String getAccessToken(String Appkey, String Appsecret) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(Appkey);
        request.setAppsecret(Appsecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = null;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.getBody());
        System.out.println(JsonUtil.parseJSONstr2Map(response.getBody()).get("access_token"));
        return JsonUtil.parseJSONstr2Map(response.getBody()).get("access_token").toString();
    }


    /**
     * 获取消息机器人的token
     */
    public static String getSendMsgToken() throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = createClient();
        com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey("dingyhlk4qgay8m8ujqt")
                .setAppSecret("pAT7ex15r6uthKFh_-d0vmSIUzmYxyecf59uoBlTj2XCfOItVyil7D4hcjCSmQkV");
        try {
            GetAccessTokenResponse x = client.getAccessToken(getAccessTokenRequest);
            return x.getBody().getAccessToken();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            return err.getMessage();

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            return _err.getMessage();
        }
    }


    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }
    public static void main(String[] args_) throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = createClient();
        com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey("dingyhlk4qgay8m8ujqt")
                .setAppSecret("pAT7ex15r6uthKFh_-d0vmSIUzmYxyecf59uoBlTj2XCfOItVyil7D4hcjCSmQkV");
        try {
            GetAccessTokenResponse x=client.getAccessToken(getAccessTokenRequest);
            x.getBody().getAccessToken();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
    }
}
