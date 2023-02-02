package com.xjrsoft.common.runner;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.xjrsoft.common.utils.TimerUtil;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.module.base.entity.XjrBaseModule;
import com.xjrsoft.module.base.service.IXjrBaseModuleService;
import com.xjrsoft.module.buildCode.props.GlobalConfigProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class StartRunner  implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
//        TimerUtil.setOutTimeTimer();

        // 修改子系统状态
        updateSubSystemModule();
    }

    private void updateSubSystemModule() {
        GlobalConfigProperties globalConfigProperties = SpringUtil.getBean(GlobalConfigProperties.class);
        IXjrBaseModuleService moduleService = SpringUtil.getBean(IXjrBaseModuleService.class);
        Integer isDeleted = globalConfigProperties.getEnabled_subSystem() ? 0 : 1;
        UpdateWrapper<XjrBaseModule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("F_DeleteMark", isDeleted);
        updateWrapper.eq("F_EnCode", "SubSystem");
        moduleService.update(updateWrapper);
    }
}
