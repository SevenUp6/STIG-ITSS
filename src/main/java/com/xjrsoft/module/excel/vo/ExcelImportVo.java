package com.xjrsoft.module.excel.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @Author:光华科技-软件研发部
* @Date:2020/11/10
* @Description:excel导入实体类
*/
@Data
public class ExcelImportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonProperty("F_Id")
    private String Id;

    /**
     * 名称
     */
    @JsonProperty("F_Name")
    private String Name;

    /**
     * 关联模块Id
     */
    @JsonProperty("F_ModuleId")
    private String ModuleId;

    /**
     * 关联按钮Id
     */
    @JsonProperty("F_ModuleBtnId")
    private String ModuleBtnId;

    /**
     * 按钮名称
     */
    @JsonProperty("F_BtnName")
    private String BtnName;

    /**
     * 导入数据库ID
     */
    @JsonProperty("F_DbId")
    private String DbId;

    /**
     * 导入数据库表
     */
    @JsonProperty("F_DbTable")
    private String DbTable;

    /**
     * 错误处理机制0终止,1跳过
     */
    @JsonProperty("F_ErrorType")
    private Integer ErrorType;

    /**
     * 是否有效0暂停,1启用
     */
    @JsonProperty("F_EnabledMark")
    private Integer EnabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String Description;

    /**
     * 创建时间
     */
    @JsonProperty("F_CreateDate")
    private LocalDateTime CreateDate;

    /**
     * 创建人Id
     */
    @JsonProperty("F_CreateUserId")
    private String CreateUserId;

    /**
     * 创建人名字
     */
    @JsonProperty("F_CreateUserName")
    private String CreateUserName;



}
