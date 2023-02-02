package com.xjrsoft.module.itss.repairGuide.vo;

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
public class RepairGuideFormDataVo {
    private static final long serialVersionUID = 1L;

    @JsonProperty("repair_guide")
    private RepairGuideVo repairGuideVo;

    @JsonProperty("xjr_base_annexesfile")
    private List<XjrBaseAnnexesfileVo> xjrBaseAnnexesfileVoList;


}
