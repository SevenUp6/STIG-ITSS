package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.xjrsoft.common.cache.CacheAble;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据源表
 * </p>
 *
 * @author jobob
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_datasource")
public class XjrBaseDatasource implements CacheAble,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 编号
     */
    @TableField("F_Code")
    private String code;

    /**
     * 名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 数据库主键
     */
    @TableField("F_DbId")
    private String dbId;

    /**
     * sql语句
     */
    @TableField("F_Sql")
    private String fsql;

    /**
     * b备注
     */
    @TableField(value = "F_Description")
    private String description;

    /**
     * 创建人主键
     */
    @TableField(value = "F_CreateUserId",fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人名字
     */
    @TableField(value = "F_CreateUserName",fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate",fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改人主键
     */
    @TableField(value = "F_ModifyUserId",fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改人名字
     */
    @TableField(value = "F_ModifyUserName",fill = FieldFill.UPDATE)
    private String modifyUserName;

    /**
     * 修改日期
     */
    @TableField(value = "F_ModifyDate",fill = FieldFill.UPDATE)
    private LocalDateTime modifyDate;

    public String getCacheId() {
        return this.id;
    }
}
