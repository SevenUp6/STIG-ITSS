package com.xjrsoft.module.itss.fAQuestion.vo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
    
/**
 * 保存常见问题表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
public class FAQuestionFormDataVo {
    private static final long serialVersionUID = 1L;


    private FAQuestionVo fAQuestionVo;

    @JsonProperty("xjr_base_annexesfile")
    private List<XjrBaseAnnexesfileVo> xjrBaseAnnexesfileVoList;


}
