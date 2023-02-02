package com.xjrsoft.module.base.service;

import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDbDraft;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.vo.DbDraftVo;

import java.util.List;

/**
 * <p>
 * 数据表草稿 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
public interface IXjrBaseDbDraftService extends IService<XjrBaseDbDraft> {

    PageOutput<DbDraftVo> getDbDraftPageList(GetPageListDto dto);
}
