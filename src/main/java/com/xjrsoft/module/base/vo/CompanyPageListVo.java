package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CompanyPageListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公司主键
     */
    @JsonProperty("F_CompanyId")
    private String companyId;

    /**
     * 公司分类
     */
    @JsonProperty("F_Category")
    private Integer category;

    /**
     * 父级主键
     */
    @JsonProperty("F_ParentId")
    private String parentId;

    /**
     * 公司代码
     */
    @JsonProperty("F_EnCode")
    private String enCode;

    /**
     * 公司简称
     */
    @JsonProperty("F_ShortName")
    private String shortName;

    /**
     * 公司名称
     */
    @JsonProperty("F_FullName")
    private String fullName;

    /**
     * 公司性质
     */
    @JsonProperty("F_Nature")
    private String nature;

    /**
     * 外线电话
     */
    @JsonProperty("F_OuterPhone")
    private String outerPhone;

    /**
     * 内线电话
     */
    @JsonProperty("F_InnerPhone")
    private String innerPhone;

    /**
     * 传真
     */
    @JsonProperty("F_Fax")
    private String fax;

    /**
     * 邮编
     */
    @JsonProperty("F_Postalcode")
    private String postalCode;

    /**
     * 电子邮箱
     */
    @JsonProperty("F_Email")
    private String email;

    /**
     * 负责人
     */
    @JsonProperty("F_Manager")
    private String manager;

    /**
     * 省主键
     */
    @JsonProperty("F_ProvinceId")
    private String provinceId;

    /**
     * 市主键
     */
    @JsonProperty("F_CityId")
    private String cityId;

    /**
     * 县/区主键
     */
    @JsonProperty("F_CountyId")
    private String countyId;

    /**
     * 详细地址
     */
    @JsonProperty("F_Address")
    private String address;

    /**
     * 公司主页
     */
    @JsonProperty("F_WebAddress")
    private String webAddress;

    /**
     * 成立时间
     */
    @JsonProperty("F_FoundedTime")
    private LocalDateTime foundedTime;

    /**
     * 经营范围
     */
    @JsonProperty("F_BusinessScope")
    private String businessScope;

    /**
     * 排序码
     */
    @JsonProperty("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @JsonProperty("F_DeleteMark")
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @JsonProperty("F_EnabledMark")
    private Integer enabledMark;

    /**
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;


}
