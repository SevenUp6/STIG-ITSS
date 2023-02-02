package com.xjrsoft.module.excel.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@ApiModel(value = "ExcelExportDto", description = "excel导出dto")
public class ExcelExportDto {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId("F_Id")
    @JsonProperty("F_Id")
    private String id;

    /**
     * 文件名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 绑定的JQgirdId
     */
    @JsonProperty("F_GridId")
    private String gridId;

    /**
     * 功能模块Id
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

    /**
     * 按钮Id
     */
    @JsonProperty("F_ModuleBtnId")
    private String moduleBtnId;

    /**
     * 按钮名称
     */
    @JsonProperty("F_BtnName")
    private String btnName;

    /**
     * 是否有效
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 创建时间
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建人ID
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建人姓名
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    /**
     * 修改日期
     */
    @JsonProperty("F_ModifyDate")
    private LocalDateTime modifyDate;

    /**
     * 修改人Id
     */
    @JsonProperty("F_ModifyUserId")
    private String modifyUserId;

    /**
     * 修改人名称
     */
    @JsonProperty("F_ModifyUserName")
    private String modifyUserName;


}
