package com.xjrsoft.module.itss.fAQuestion.dto;

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
public class SaveFAQuestionFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("f_a_questionEntity")
    private FAQuestionDto fAQuestionDto;

    @JsonProperty("xjr_base_annexesfileEntityList")
    private List<XjrBaseAnnexesfileDto> xjrBaseAnnexesfileDto;

    @JsonProperty("xjr_base_annexesfile")
    private List<XjrBaseAnnexesfileDto> AnnexesfileDto_modify;
}
