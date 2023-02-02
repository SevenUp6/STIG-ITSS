package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.common.allenum.DeleteMarkEnum;
import com.xjrsoft.common.core.VoToColumn;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.base.dto.GetLogPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDbField;
import com.xjrsoft.module.base.entity.XjrBaseLog;
import com.xjrsoft.module.base.mapper.XjrBaseLogMapper;
import com.xjrsoft.module.base.service.IXjrBaseLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.module.base.vo.LogPageListVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Service
public class XjrBaseLogServiceImpl extends ServiceImpl<XjrBaseLogMapper, XjrBaseLog> implements IXjrBaseLogService {

    @Override
    public PageOutput<LogPageListVo> getLogPageList(GetLogPageListDto dto) {
        String keyword = dto.getKeyword();
        LambdaQueryWrapper<XjrBaseLog> queryWrapper = Wrappers.<XjrBaseLog>query().lambda().like(StringUtil.isNotBlank(keyword), XjrBaseLog::getModule, keyword);
        IPage<XjrBaseLog> xjrBaseLogIPage = baseMapper.selectPage(ConventPage.getPage(dto), queryWrapper
                .select(XjrBaseLog.class, x -> VoToColumn.Convert(LogPageListVo.class).contains(x.getColumn()))
                .eq(XjrBaseLog::getDeleteMark, DeleteMarkEnum.NODELETE.getCode())
                .eq(dto.getCategoryId() != 0, XjrBaseLog::getCategoryId, dto.getCategoryId())
                .between(dto.getStartTime() != null && dto.getEndTime() != null, XjrBaseLog::getOperateTime, dto.getStartTime(), dto.getEndTime())
                .orderByDesc(XjrBaseLog::getOperateTime));

        return ConventPage.getPageOutput(xjrBaseLogIPage, LogPageListVo.class);
    }

    @Override
    public Boolean clearLog(int type) {
        switch (type){
            case 1:
              return  baseMapper.delete(Wrappers.<XjrBaseLog>query().lambda().lt(XjrBaseLog::getOperateTime, LocalDateTime.now().minusDays(7))) > 0;
            case 2:
               return  baseMapper.delete(Wrappers.<XjrBaseLog>query().lambda().lt(XjrBaseLog::getOperateTime, LocalDateTime.now().minusDays(30))) > 0;
            case 3:
                return  baseMapper.delete(Wrappers.<XjrBaseLog>query().lambda().lt(XjrBaseLog::getOperateTime, LocalDateTime.now().minusDays(90))) > 0;
            case  4:
                return  baseMapper.delete(Wrappers.<XjrBaseLog>query().lambda().ne(XjrBaseLog::getLogId, "")) > 0;
        }
        return false;
    }

    @Override
    public Boolean addLog(XjrBaseLog log) {
        return this.save(log);
    }
}
