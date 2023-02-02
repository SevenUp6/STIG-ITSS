package com.xjrsoft.module.itss.fAQuestion.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.itss.fAQuestion.entity.FAQuestion;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.module.itss.fAQuestion.entity.XjrBaseAnnexesfile;
import com.xjrsoft.module.itss.fAQuestion.mapper.XjrBaseAnnexesfileMapper;
import com.xjrsoft.module.itss.fAQuestion.dto.FAQuestionListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjrsoft.common.page.ConventPage;

import java.time.LocalDateTime;
import java.util.List;
import com.xjrsoft.module.itss.fAQuestion.mapper.FAQuestionMapper;
import com.xjrsoft.module.itss.fAQuestion.service.IFAQuestionService;
import com.xjrsoft.core.mp.base.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 常见问题表 服务实现类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Service
@AllArgsConstructor
public class FAQuestionServiceImpl extends BaseService<FAQuestionMapper, FAQuestion> implements IFAQuestionService {

	private XjrBaseAnnexesfileMapper xjrBaseAnnexesfileMapper;

	@Override
	public IPage<FAQuestion> getPageList(FAQuestionListDto pageListDto) {
		Wrapper<FAQuestion> wrapper = Wrappers.<FAQuestion>query().lambda()				.like(!StringUtil.isEmpty(pageListDto.getProblem_des()), FAQuestion::getProblemDes, pageListDto.getProblem_des())
				.like(!StringUtil.isEmpty(pageListDto.getMod_id()), FAQuestion::getModId, pageListDto.getMod_id())
				.like(!StringUtil.isEmpty(pageListDto.getMod_name()), FAQuestion::getModName, pageListDto.getMod_name())
				.like(!StringUtil.isEmpty(pageListDto.getFault_name()), FAQuestion::getFaultName, pageListDto.getFault_name())
				.like(!StringUtil.isEmpty(pageListDto.getSuggestion()), FAQuestion::getSuggestion, pageListDto.getSuggestion());
		return this.page(ConventPage.getPage(pageListDto), wrapper);
	}

	@Override
	public String addFAQuestion(FAQuestion fAQuestion) {
		String fAQuestionId = IdWorker.get32UUID();
		String userId = SecureUtil.getUserId();
		fAQuestion.setCreatedBy(userId);
		fAQuestion.setCreatedName(SecureUtil.getUserName());;
		fAQuestion.setCreatedTime(LocalDateTime.now());
		fAQuestion.setId(fAQuestionId);
//		xjrBaseAnnexesfileList.forEach(xjrBaseAnnexesfile -> xjrBaseAnnexesfile.setFFolderid(fAQuestionId));
//		this.saveBatch(xjrBaseAnnexesfileList, XjrBaseAnnexesfile.class, XjrBaseAnnexesfileMapper.class);
		boolean isSuccess = this.save(fAQuestion);
		if(isSuccess){
			return fAQuestionId;
		}else {
			return "error";
		}
	}

	@Override
	public boolean updateFAQuestion(String id, FAQuestion fAQuestion, List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList) {
		fAQuestion.setId(id);
//		xjrBaseAnnexesfileList.forEach(xjrBaseAnnexesfile -> xjrBaseAnnexesfile.setFFolderid(id));
//		xjrBaseAnnexesfileMapper.delete(Wrappers.<XjrBaseAnnexesfile>query().lambda().eq(XjrBaseAnnexesfile::getFFolderid, id));
//		this.saveBatch(xjrBaseAnnexesfileList, XjrBaseAnnexesfile.class, XjrBaseAnnexesfileMapper.class);
		return this.updateById(fAQuestion);
	}

	public List<XjrBaseAnnexesfile> getXjrBaseAnnexesfileByParentId(String parentId){
		Wrapper wrapper = Wrappers.<XjrBaseAnnexesfile>query().lambda().eq(XjrBaseAnnexesfile::getFFolderid, parentId);
		return xjrBaseAnnexesfileMapper.selectList(wrapper);
	}
}
