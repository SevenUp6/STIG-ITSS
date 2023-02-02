package com.xjrsoft.module.itss.repairGuide.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 保存维修指南表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
public class SaveRepairGuideFormDataDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("repair_guideEntity")
    private RepairGuideDto repairGuideDto;

    @JsonProperty("xjr_base_annexesfileEntityList")
    private List<XjrBaseAnnexesfileDto> xjrBaseAnnexesfileDto;

    @JsonProperty("xjr_base_annexesfile")
    private List<XjrBaseAnnexesfileDto> AnnexesfileDto_modify;


}
