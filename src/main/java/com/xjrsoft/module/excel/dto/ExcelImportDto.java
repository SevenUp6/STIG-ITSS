package com.xjrsoft.module.excel.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@ApiModel(value = "ExcelImportFieldsDto", description = "excel导入dto")
public class ExcelImportDto {
    /**
     * 主键
     */
    @TableId("F_Id")
    @JsonProperty("F_Id")
    private String id;

    /**
     * 名称
     */
    @JsonProperty("F_Name")
    private String name;

    /**
     * 关联模块Id
     */
    @JsonProperty("F_ModuleId")
    private String moduleId;

    /**
     * 关联按钮Id
     */
    @JsonProperty("F_ModuleBtnId")
    private String moduleBtnId;

    /**
     * 按钮名称
     */
    @JsonProperty("F_BtnName")
    private String btnName;

    /**
     * 导入数据库ID
     */
    @JsonProperty("F_DbId")
    private String dbId;

    /**
     * 导入数据库表
     */
    @JsonProperty("F_DbTable")
    private String dbTable;

    /**
     * 错误处理机制0终止,1跳过
     */
    @JsonProperty("F_ErrorType")
    private Integer errorType;

    /**
     * 是否有效0暂停,1启用
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    /**
     * 创建时间
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime createDate;

    /**
     * 创建人Id
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建人名字
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    /**
     * 修改时间
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
