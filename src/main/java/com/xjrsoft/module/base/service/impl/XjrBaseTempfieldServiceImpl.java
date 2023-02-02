package com.xjrsoft.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.module.base.dto.GetPageListDto;
import com.xjrsoft.module.base.entity.XjrBaseTempfield;
import com.xjrsoft.module.base.mapper.XjrBaseTempfieldMapper;
import com.xjrsoft.module.base.service.IXjrBaseTempfieldService;
//import com.xjrsoft.module.workflow.dto.GetPageListNwfTaskDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author:湘北智造-框架开发组
 * @Date:2020/10/26
 * @Description:临时变量表服务实现类
 */
@Service
public class XjrBaseTempfieldServiceImpl extends ServiceImpl<XjrBaseTempfieldMapper, XjrBaseTempfield> implements IXjrBaseTempfieldService {

    @Override
    public XjrBaseTempfield getByIdAndType(String idValue, String type) {
        QueryWrapper<XjrBaseTempfield> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(XjrBaseTempfield::getFkey, idValue).eq(XjrBaseTempfield::getType, type);
        List<XjrBaseTempfield> xjrBaseTempfields = baseMapper.selectList(queryWrapper);
        Optional<XjrBaseTempfield> option = xjrBaseTempfields.stream().findFirst();
        if(option.isPresent()) {
            return option.get();
        }else {
            return null;
        }
    }

    @Override
    public List<XjrBaseTempfield> getListByIdAndType(List<String> idValues, String type) {
        QueryWrapper<XjrBaseTempfield> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(XjrBaseTempfield::getType, type).in(XjrBaseTempfield::getFkey, idValues);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean saveSubProcessInfo(XjrBaseTempfield subProcess_img, XjrBaseTempfield subProcess_formData, XjrBaseTempfield subProcess_name) {
        return false;
    }

    @Override
    public List<XjrBaseTempfield> getSubProcessInfo(String idValue) {
        return null;
    }

    /**
    * @Author:湘北智造-框架开发组
    * @Date:2020/11/25
    * @Param:[nwfSchemeInfoId, deploymentId]
    * @return:java.util.List<com.xjrsoft.module.base.entity.XjrBaseTempfield>
    * @Description:根据key中是否存在流程模板信息id和部署id去查找记录
    */
    @Override
    public List<XjrBaseTempfield> selectByNwfInfoIdOrDeplId(String nwfSchemeInfoId, String deploymentId) {
        QueryWrapper<XjrBaseTempfield> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotBlank(nwfSchemeInfoId), XjrBaseTempfield::getFkey, nwfSchemeInfoId)
                .or(StringUtils.isNotBlank(deploymentId)).like(StringUtils.isNotBlank(deploymentId), XjrBaseTempfield::getFkey, deploymentId);
        return baseMapper.selectList(query);

    }

//    @Override
//    public IPage<XjrBaseTempfield> getByType(GetPageListNwfTaskDto dto, String type) {
//        QueryWrapper<XjrBaseTempfield> query = new QueryWrapper<>();
//        query.eq("F_Type", type).orderByDesc("F_Id").like(StringUtils.isNotBlank(dto.getKeyword()), "F_Key", dto.getKeyword());
//        return baseMapper.selectPage(ConventPage.getPage(dto), query);
//    }
}
