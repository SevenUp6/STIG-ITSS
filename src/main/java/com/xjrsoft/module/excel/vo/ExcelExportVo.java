package com.xjrsoft.module.excel.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ExcelExportVo implements Serializable {

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Description:excel导出视图类
     */

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
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
}
