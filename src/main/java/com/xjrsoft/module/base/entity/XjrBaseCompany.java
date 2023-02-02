package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjrsoft.common.cache.CacheAble;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构单位表
 * </p>
 *
 * @author jobob
 * @since 2020-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class XjrBaseCompany implements CacheAble,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公司主键
     */
    @TableId("F_CompanyId")
    private String companyId;

    /**
     * 公司分类
     */
    @TableField("F_Category")
    private Integer category;

    /**
     * 父级主键
     */
    @TableField("F_ParentId")
    private String parentId;

    /**
     * 公司代码
     */
    @TableField("F_EnCode")
    private String enCode;

    /**
     * 公司简称
     */
    @TableField("F_ShortName")
    private String shortName;

    /**
     * 公司名称
     */
    @TableField("F_FullName")
    private String fullName;

    /**
     * 公司性质
     */
    @TableField("F_Nature")
    private String nature;

    /**
     * 外线电话
     */
    @TableField("F_OuterPhone")
    private String outerPhone;

    /**
     * 内线电话
     */
    @TableField("F_InnerPhone")
    private String innerPhone;

    /**
     * 传真
     */
    @TableField("F_Fax")
    private String fax;

    /**
     * 邮编
     */
    @TableField("F_Postalcode")
    private String postalCode;

    /**
     * 电子邮箱
     */
    @TableField("F_Email")
    private String email;

    /**
     * 负责人
     */
    @TableField("F_Manager")
    private String manager;

    /**
     * 省主键
     */
    @TableField("F_ProvinceId")
    private String provinceId;

    /**
     * 市主键
     */
    @TableField("F_CityId")
    private String cityId;

    /**
     * 县/区主键
     */
    @TableField("F_CountyId")
    private String countyId;

    /**
     * 详细地址
     */
    @TableField("F_Address")
    private String address;

    /**
     * 公司主页
     */
    @TableField("F_WebAddress")
    private String webAddress;

    /**
     * 成立时间
     */
    @TableField("F_FoundedTime")
    private LocalDateTime foundedTime;

    /**
     * 经营范围
     */
    @TableField("F_BusinessScope")
    private String businessScope;

    /**
     * 排序码
     */
    @TableField("F_SortCode")
    private Integer sortCode;

    /**
     * 删除标记
     */
    @TableField(value = "F_DeleteMark", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteMark;

    /**
     * 有效标志
     */
    @TableField(value = "F_EnabledMark", fill = FieldFill.INSERT)
    private Integer enabledMark;

    /**
     * 备注
     */
    @TableField("F_Description")
    private String description;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建用户
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    @JsonIgnore
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改用户
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;


    @Override
    public String getCacheId() {
        return this.companyId;
    }
}
