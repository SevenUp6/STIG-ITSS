package com.xjrsoft.module.itss.fAQuestion.dto;

import com.xjrsoft.common.page.PageInput;
import lombok.Data;
    import io.swagger.annotations.ApiModel;

/**
 * 常见问题表视图实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
    @ApiModel(value = "列表FAQuestionDto对象", description = "常见问题表")
public class FAQuestionListDto extends PageInput {

    //@JsonProperty("problem_des")
    private String problem_des;


    //@JsonProperty("mod_name")
    private String mod_name;
    private String mod_id;


    //@JsonProperty("fault_name")
    private String fault_name;


    //@JsonProperty("suggestion")
    private String suggestion;


}
