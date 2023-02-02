package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zwq
 * @since 2021-06-29
 */
@Data
public class DraftVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @JsonProperty("F_Id")
    private String id;

    /**
     * 模板id
     */
    @JsonProperty("F_SchemeinfoId")
    private String schemeinfoId;

    /**
     * 模板名
     */
    @JsonProperty("F_SchemeinfoName")
    private String schemeinfoName;

    /**
     * 表单数据
     */
    @JsonProperty("F_Value")
    private String value;

    /**
     * 创建时间
     */
    @JsonProperty("F_CreateDate")
    private Date createDate;

    /**
     * 创建者id
     */
    @JsonProperty("F_CreateUserId")
    private String createUserId;

    /**
     * 创建用户名
     */
    @JsonProperty("F_CreateUserName")
    private String createUserName;

    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 修改日期
     */
    @JsonProperty("F_ModifyDate")
    private Date modifyDate;

    /**
     * 修改用户id
     */
    @JsonProperty("F_ModifyUserId")
    private String modifyUserId;

    /**
     * 修改用户名字
     */
    @JsonProperty("F_ModifyUserName")
    private String modifyUserName;


}
