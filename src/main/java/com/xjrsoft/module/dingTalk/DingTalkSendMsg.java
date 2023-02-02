package com.xjrsoft.module.dingTalk;

import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.xjrsoft.module.itss.statistics.service.StatisticsService;
import com.xjrsoft.module.itss.statistics.vo.StatisticsGdslVo;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/dingtalk")
@Api(value = "/dingtalk",tags = "钉钉相关")
public class DingTalkSendMsg {

    private  StatisticsService statisticsService;


    /**
     * 获取消息机器人的token
     */
    public static String getSendMsgToken() throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = DingTalkUtils.createClient_oauth2_1_0();
        com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey("dingccqyqm1horlijjzw")
                .setAppSecret("JKdm79jvAWI7UcQQLLTkth7qsbWFGvnw7vb3PQ7_LU_bpuiP1QNCDdpTceao4A50");
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



    public static boolean sendMsg(String dingTalkId,String name) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dingtalkrobot_1_0.Client client = DingTalkUtils.createClient_robot_1_0();
        BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();

        batchSendOTOHeaders.xAcsDingtalkAccessToken = getSendMsgToken();
        BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest()
                .setRobotCode("dingccqyqm1horlijjzw")
                .setUserIds(java.util.Arrays.asList(
                        dingTalkId
                ))
                .setMsgKey("sampleMarkdown")
                .setMsgParam("{\"text\":"+"\""+name+"您好，您有一条待处理的维修工单，请尽快处理。详细维修信息请登录光华维修运维平台查看。\",\"title\": \"您有新的派单信息\"}");
        try {
           BatchSendOTOResponse x= client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
            return true;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            return false;
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            return false;
        }

    }

    public boolean sendStaticsMsg(String[] dingTalkId) throws Exception {
        List<StatisticsGdslVo> GdslData =statisticsService.getGdslData();
        String yesterday=   LocalDate.now().minusDays(1).toString();
        String jsonString = JSON.toJSONString(GdslData);
        StringBuilder sb=new StringBuilder("{\"text\":"+"\""+"各位领导上午好， ")
                .append(yesterday).append("  各运维系统及设备故障维修情况如下：");
//		("{\"text\":"+"\""+"给位领导上午好，"+year+"各运维系统及设备故障维修情况如下：\",\"title\": \"昨日维修情况\"}");
        int i=0;
        for (StatisticsGdslVo statisticsGdslVo:GdslData){
            i++;
            sb.append(i).append(".").append(statisticsGdslVo.getTypeName()).append("：新增的总工单：").append(statisticsGdslVo.getSum()).append("条；已处理：").append(statisticsGdslVo.getSum9()).append(";未处理").append(statisticsGdslVo.getSum0()).append("。");
        }
        sb.append("\",\"title\": \"昨日维修工单情况\"}");
        com.aliyun.dingtalkrobot_1_0.Client client = DingTalkUtils.createClient_robot_1_0();
        BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
        batchSendOTOHeaders.xAcsDingtalkAccessToken = getSendMsgToken();
        BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest()
                .setRobotCode("dingccqyqm1horlijjzw")
                .setUserIds(java.util.Arrays.asList(
                        dingTalkId
                ))
                .setMsgKey("sampleMarkdown")
                .setMsgParam(sb.toString());
        try {
           BatchSendOTOResponse x= client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
            return true;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            return false;
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            return false;
        }
    }
}
