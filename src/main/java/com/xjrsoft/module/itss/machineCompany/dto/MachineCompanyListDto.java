package com.xjrsoft.module.itss.machineCompany.dto;

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
 * 设备所属单位表视图实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
    @ApiModel(value = "列表MachineCompanyDto对象", description = "设备所属单位表")
public class MachineCompanyListDto extends PageInput {

    //@JsonProperty("com_name")
    private String com_name;

    private String pcode;

    private String com_code;


}
