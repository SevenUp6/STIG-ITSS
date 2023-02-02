package com.xjrsoft.module.base.service.impl;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xjrsoft.module.base.entity.XjrBaseAnnexesFile;

import com.xjrsoft.module.base.mapper.XjrBaseAnnexesfileMapper;
import com.xjrsoft.module.base.service.IXjrBaseAnnexesFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 文件关联关系表 服务实现类
 *
 * @author jobob
 * @since 2020-12-22
 */
@Service
@AllArgsConstructor
@DS("")
public class XjrBaseAnnexesFileServiceImpl extends ServiceImpl<XjrBaseAnnexesfileMapper, XjrBaseAnnexesFile> implements IXjrBaseAnnexesFileService {

}
