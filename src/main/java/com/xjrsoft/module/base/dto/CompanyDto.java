package com.xjrsoft.module.base.dto;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode()
public class CompanyDto {
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
     * 备注
     */
    @JsonProperty("F_Description")
    private String description;

    @JsonProperty("postUserJson")
    private Map<String, List<String>> postUserJson;
}
