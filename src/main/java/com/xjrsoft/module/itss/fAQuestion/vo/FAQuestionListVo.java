package com.xjrsoft.module.itss.fAQuestion.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.Enum.TransDataType;
import com.xjrsoft.common.annotation.DataTrans;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * 常见问题表视图实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
@ApiModel(value = "列表FAQuestionVo对象", description = "常见问题表")
public class FAQuestionListVo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("problem_des")
    private String problemDes;

    @JsonProperty("mod_name")
    private String modName;

    @JsonProperty("mod_id")
    private String modId;

    @JsonProperty("fault_name")
    private String faultName;

    @JsonProperty("fault_id")
    private String faultId;

    @JsonProperty("suggestion")
    private String suggestion;

    @DataTrans(dataType = TransDataType.USER)
    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    @DataTrans(dataType = TransDataType.USER)
    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("updated_time")
    private LocalDateTime updatedTime;

    @JsonProperty("created_name")
    private String createdName;

}
