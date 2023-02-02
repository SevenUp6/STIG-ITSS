package com.xjrsoft.module.itss.repairGuide.dto;

import com.xjrsoft.common.page.PageInput;
import lombok.Data;
    import io.swagger.annotations.ApiModel;

/**
 * 维修指南表视图实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
    @ApiModel(value = "列表RepairGuideDto对象", description = "维修指南表")
public class RepairGuideListDto extends PageInput {

    //@JsonProperty("guide_name")
    private String guide_name;


    //@JsonProperty("mod_name")
    private String mod_name;
    private String mod_id;


    //@JsonProperty("fault_name")
    private String fault_name;


}
