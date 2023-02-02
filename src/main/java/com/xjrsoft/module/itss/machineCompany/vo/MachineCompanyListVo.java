package com.xjrsoft.module.itss.machineCompany.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * 设备所属单位表视图实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
@ApiModel(value = "列表MachineCompanyVo对象", description = "设备所属单位表")
public class MachineCompanyListVo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("com_name")
    private String comName;

}
