package com.xjrsoft.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageInput;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.core.secure.XjrUser;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.*;
import com.xjrsoft.module.base.mapper.XjrBaseCoderuleMapper;
import com.xjrsoft.module.base.service.*;
import com.xjrsoft.module.base.utils.OrganizationCacheUtil;
import com.xjrsoft.module.base.vo.CodeRuleVo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 编号规则表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Service
@AllArgsConstructor
public class XjrBaseCodeRuleServiceImpl extends ServiceImpl<XjrBaseCoderuleMapper, XjrBaseCoderule> implements IXjrBaseCodeRuleService {

    private final IXjrBaseCodeRuleSeedService codeRuleSeedService;

    private final IXjrBaseCompanyService companyService;

    private final IXjrBaseDepartmentService departmentService;

    private final IXjrBaseUserRelationService userRelationService;

    private final IXjrBaseUserService userService;

    @Override
    public XjrBaseCoderule getCodeRuleById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public PageOutput<CodeRuleVo> getCodeRulePageList(PageInput dto) {
        QueryWrapper<XjrBaseCoderule> query = new QueryWrapper<>();
//        query.select(VoToColumn.Convert(CodeRuleVo.class));
        String keyword = dto.getKeyword();
        if (!StringUtil.isEmpty(keyword)) {
            keyword = StringPool.PERCENT + keyword + StringPool.PERCENT;
        }
        query.lambda().like(!StrUtil.hasBlank(keyword),XjrBaseCoderule::getFullName, keyword);
        IPage<XjrBaseCoderule> page = baseMapper.selectPage(ConventPage.getPage(dto), query);
        return ConventPage.getPageOutput(page, CodeRuleVo.class);
    }

    @Override
    public String genEncode(String encode) {
        XjrBaseCoderule codeRule = this.getCodeRuleByEncode(encode);
        if (codeRule == null) {
            return null;
        }
        XjrBaseCodeRuleSeed codeRuleSeed = null;
        XjrUser user = SecureUtil.getUser();
        if (user != null) {
            // 查询用户已存在的编码
            codeRuleSeed = codeRuleSeedService.getCodeRuleSeedBy(codeRule.getRuleId(), user.getUserId());
            if (codeRuleSeed != null) {
                // 返回用户已存在的编码
                return genEnCode(codeRule, codeRuleSeed, false);
            }
        }
        if (codeRuleSeed == null) {
            // 查询最新的编码
            List<XjrBaseCodeRuleSeed> list = codeRuleSeedService.list(Wrappers.<XjrBaseCodeRuleSeed>query().lambda()
                    .eq(XjrBaseCodeRuleSeed::getRuleId, codeRule.getRuleId()).isNull(XjrBaseCodeRuleSeed::getUserId));
            if (CollectionUtil.isNotEmpty(list)) {
                codeRuleSeed = list.get(0);
            } else {
                codeRuleSeed = new XjrBaseCodeRuleSeed();
                codeRuleSeed.setRuleId(codeRule.getRuleId());
            }
            String userCode = genEnCode(codeRule, codeRuleSeed, true);
            // 保存下一个编码和种子
            if (this.updateById(codeRule)) {
                return codeRuleSeedService.saveOrUpdate(codeRuleSeed) ? userCode : null;
            }
        }
        return null;
    }

    @Override
    public boolean useEncode(String encode) {
        XjrBaseCoderule codeRule = this.getCodeRuleByEncode(encode);
        if (codeRule == null) {
            return  false;
        }
        XjrBaseCodeRuleSeed codeRuleSeed = codeRuleSeedService.getCodeRuleSeedBy(codeRule.getRuleId(), SecureUtil.getUserId());
        if (codeRuleSeed != null) {
            return codeRuleSeedService.removeById(codeRuleSeed.getRuleSeedId());
        }
        return false;
    }

    private String genEnCode(XjrBaseCoderule codeRule, XjrBaseCodeRuleSeed currentSeed, boolean isGenNext) {
        List<XjrBaseUser> userList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.USER_LIST_CACHE_KEY);
        List<XjrBaseCompany> companyList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.COMPANY_LIST_CACHE_KEY);
        List<XjrBaseDepartment> departmentList = OrganizationCacheUtil.getListCaches(OrganizationCacheUtil.DEPARTMENT_LIST_CACHE_KEY);
        String ruleFormat = codeRule.getRuleFormatJson();
        JSONArray rleFormatJson = JSONArray.parseArray(ruleFormat);
        StringBuilder currentCode = new StringBuilder();
        StringBuilder nextCode = new StringBuilder();
        XjrBaseUser user = null;
        // 下一个种子值
        Integer nextSeedValue = null;
        String userId = SecureUtil.getUserId();
        for (Object obj : rleFormatJson) {
            JSONObject json = (JSONObject) obj;
            String itemType = json.getString("itemType");
            String formatStr = json.getString("formatStr");
            switch (itemType) {
                case "0":	//自定义
                    currentCode.append(formatStr);
                    nextCode.append(formatStr);
                    break;
                case "1":	//日期
                    // 转换成java的时间格式
                    String dateFormat = StringUtils.replace(StringUtils.lowerCase(formatStr), "m", "M");
                    String dateStr = DateUtil.format(new Date(), dateFormat);
                    currentCode.append(dateStr);
                    nextCode.append(dateStr);
                    break;
                case "2":	//流水号
                    Integer seedValue = currentSeed.getSeedValue();
                    if (seedValue == null) {
                        seedValue = json.getIntValue("initValue");
                        currentSeed.setSeedValue(seedValue);
                    }
                    // 计算下一个种子值
                    Integer stepValue = json.getInteger("stepValue");
                    nextSeedValue = seedValue + stepValue;
                    // 流水号到达最大值，重置
                    Integer maxValue = Integer.valueOf(StringUtil.replace(formatStr, StringPool.ZERO, "9"));
                    if (nextSeedValue >= maxValue) {
                        nextSeedValue = json.getInteger("initValue");
                    }
                    // 生成当前流水号
                    currentCode.append(StringUtil.formatNumber(seedValue, formatStr));
                    // 生成下一个流水号
                    nextCode.append(StringUtil.formatNumber(nextSeedValue, formatStr));
                    break;
                case "3":	//公司
                    if (user == null) {
                        Optional<XjrBaseUser> userOptional = userList.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
                        if(userOptional.isPresent()){
                            user =userOptional.get();
                        }
                    }
                    String companyId = user.getCompanyId();
                    Optional<XjrBaseCompany> companyOptional = companyList.stream().filter(x -> x.getCompanyId().equals(companyId)).findFirst();
                    if (!companyOptional.isPresent()) {
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase("name", formatStr)) {
                        currentCode.append(companyOptional.get().getFullName());
                        nextCode.append(companyOptional.get().getFullName());
                    } else {
                        currentCode.append(companyOptional.get().getEnCode());
                        nextCode.append(companyOptional.get().getEnCode());
                    }
                    break;
                case "4":	//部门
                    XjrBaseUserRelation userRelation = userRelationService.getOne(Wrappers.<XjrBaseUserRelation>query().lambda()
                            .eq(XjrBaseUserRelation::getUserId, userId).eq(XjrBaseUserRelation::getCategory, 3)
                            .orderByDesc(XjrBaseUserRelation::getCreateDate), false);
                    if (userRelation == null) {
                        break;
                    }
                    String objectId = userRelation.getObjectId();
                    Optional<XjrBaseDepartment> departmentOptional = departmentList.stream().filter(x -> x.getDepartmentId().equals(objectId)).findFirst();
                    if (!departmentOptional.isPresent()) {
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase("name", formatStr)) {
                        currentCode.append(departmentOptional.get().getFullName());
                        nextCode.append(departmentOptional.get().getFullName());
                    } else {
                        currentCode.append(departmentOptional.get().getEnCode());
                        nextCode.append(departmentOptional.get().getEnCode());
                    }
                    break;
                case "5":	//用户
                    if (user == null) {
                        Optional<XjrBaseUser> userOptional = userList.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
                        if(userOptional.isPresent()) {
                            user = userOptional.get();
                        }
                    }
                    if (StringUtils.equalsIgnoreCase("name", formatStr)) {
                        String realName = SecureUtil.getUserName();
                        if (StringUtil.isEmpty(realName)) {
                            break;
                        }
                        currentCode.append(realName);
                        nextCode.append(realName);
                    } else {
                        String userEncode = user.getEnCode();
                        if (StringUtil.isEmpty(userEncode)) {
                            break;
                        }
                        currentCode.append(userEncode);
                        nextCode.append(userEncode);
                    }
                    break;
            }
        }
        if (!isGenNext) {
            // 不生成下一个编码，只返回当前生成的编码
            return currentCode.toString();
        }
        // 保存分配给用户的编码
        XjrBaseCodeRuleSeed userRuleSeed = BeanUtil.copy(currentSeed, XjrBaseCodeRuleSeed.class);
        userRuleSeed.setRuleSeedId(IdWorker.get32UUID());
        userRuleSeed.setUserId(userId);
        codeRuleSeedService.save(userRuleSeed);
        // 设置下一个编码和种子值到对象中
        currentSeed.setSeedValue(nextSeedValue);
        codeRule.setCurrentNumber(nextCode.toString());
        return currentCode.toString();
    }

    public XjrBaseCoderule getCodeRuleByEncode(String encode) {
        return this.getOne(Wrappers.<XjrBaseCoderule>query().lambda()
                .eq(XjrBaseCoderule::getEnCode, encode).eq(XjrBaseCoderule::getEnabledMark, 1), false);
    }
}
