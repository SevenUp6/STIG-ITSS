package com.xjrsoft.module.base.service;

import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.module.base.dto.GetLogPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.vo.LogPageListVo;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
public interface IXjrBaseLogService extends IService<XjrBaseLog> {

    PageOutput<LogPageListVo> getLogPageList(GetLogPageListDto dto);

    Boolean clearLog(int type);

    Boolean addLog(XjrBaseLog log);

}
