package com.xjrsoft.module.itss;

import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
import com.xjrsoft.module.base.vo.MemberUserVo;
import com.xjrsoft.module.base.vo.UserVo;
import com.xjrsoft.module.dingTalk.DingTalkSendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private IXjrBaseUserRelationService userRelationService;
    @Autowired
    private DingTalkSendMsg dingTalkSendMsg;

    //3.添加定时任务
    @Scheduled(cron = "0 0 8 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() throws Exception {
        //获取userid
        List<UserVo> userVoList = userRelationService.getMemberUserVoListOfObject("4694ef1f4492c64f5ada0c6da6490367", 1, null);
        String [] userids=new String[userVoList.size()];
        List<MemberUserVo> memberUserVoList = BeanUtil.copyList(userVoList, MemberUserVo.class);
        int i=0;
        for(MemberUserVo userVo:memberUserVoList){
            userids[i]=userVo.getDingTalkId();
            i++;
        }
        dingTalkSendMsg.sendStaticsMsg(userids);
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
}
