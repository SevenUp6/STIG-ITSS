package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据权限对应表
 * </p>
 *
 * @author jobob
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_datarelation")
public class XjrBaseDatarelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 名称
     */
    @TableField("F_Name")
    private String name;

    /**
     * 1.普通权限 2.自定义表单权限
     */
    @TableField("F_Type")
    private Integer type;

    /**
     * 对应模块主键
     */
    @TableField("F_InterfaceId")
    private String interfaceId;

    /**
     * 对象主键
     */
    @TableField("F_ObjectId")
    private String objectId;

    /**
     * 对象类型1.角色2.用户
     */
    @TableField("F_ObjectType")
    private Integer objectType;

    /**
     * 条件公式
     */
    @TableField("F_Formula")
    private String formula;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建用户主键
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建用户名字
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 修改时间
     */
    @TableField(value = "F_ModifyDate", fill = FieldFill.UPDATE)
    private LocalDateTime modifyDate;

    /**
     * 修改用户主键
     */
    @TableField(value = "F_ModifyUserId", fill = FieldFill.UPDATE)
    private String modifyUserId;

    /**
     * 修改用户名字
     */
    @TableField(value = "F_ModifyUserName", fill = FieldFill.UPDATE)
    private String modifyUserName;


}
