package com.xjrsoft.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjrsoft.module.base.entity.XjrBaseSubsystem;

import java.util.List;

/**
 * 子系统表 Mapper 接口
 *
 * @author Job
 * @since 2021-06-08
 */
public interface XjrBaseSubsystemMapper extends BaseMapper<XjrBaseSubsystem> {

    List<XjrBaseSubsystem> selectAuthList(String userId);

    // 1-存在  0-不存在
    int checkMainSystemAuth(String userId);
}
