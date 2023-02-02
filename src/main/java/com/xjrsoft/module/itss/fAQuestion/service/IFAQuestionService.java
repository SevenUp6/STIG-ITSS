package com.xjrsoft.module.itss.fAQuestion.service;

import com.xjrsoft.module.itss.fAQuestion.entity.FAQuestion;
import com.xjrsoft.module.itss.fAQuestion.entity.XjrBaseAnnexesfile;
import com.xjrsoft.module.itss.fAQuestion.dto.FAQuestionListDto;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 常见问题表 服务类
 *
 * @author hanhe
 * @since 2022-10-20
 */
public interface IFAQuestionService extends IService<FAQuestion> {
	/**
	 * 自定义分页
	 *
	 * @param pageListDto
	 * @return
	 */
	IPage<FAQuestion> getPageList(FAQuestionListDto pageListDto);

	List<XjrBaseAnnexesfile> getXjrBaseAnnexesfileByParentId(String parentId);
	String addFAQuestion(FAQuestion fAQuestion );

	boolean updateFAQuestion(String id, FAQuestion fAQuestion, List<XjrBaseAnnexesfile> xjrBaseAnnexesfileList);
}
