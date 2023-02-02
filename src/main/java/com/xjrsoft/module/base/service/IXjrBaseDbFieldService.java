package com.xjrsoft.module.base.service;

import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.module.base.dto.DbFieldDto;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseDbField;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjrsoft.module.base.vo.DataSourceVo;
import com.xjrsoft.module.base.vo.DbFieldVo;

import java.util.List;

/**
 * <p>
 * 建表常用字段 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-11-10
 */
public interface IXjrBaseDbFieldService extends IService<XjrBaseDbField> {

    PageOutput<DbFieldVo> getDbDraftPageList(GetPageListDto pageListDto);

}
