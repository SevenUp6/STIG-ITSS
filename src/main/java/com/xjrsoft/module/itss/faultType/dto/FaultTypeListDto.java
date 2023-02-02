package com.xjrsoft.module.itss.faultType.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.common.page.PageInput;
import lombok.Data;
    import io.swagger.annotations.ApiModel;

/**
 * 故障类型表视图实体类
 *
 * @author hanhe
 * @since 2022-10-12
 */
@Data
    @ApiModel(value = "列表FaultTypeDto对象", description = "故障类型表")
public class FaultTypeListDto extends PageInput {

    //@JsonProperty("fau_name")
    private String fau_name;
    private String mod_name;
    private String type_name;

    private String mod_id;


}
