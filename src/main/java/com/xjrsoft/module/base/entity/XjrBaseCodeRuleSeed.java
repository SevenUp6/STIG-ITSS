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
 * 编号规则种子表
 * </p>
 *
 * @author jobob
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_coderuleseed")
public class XjrBaseCodeRuleSeed implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号规则种子主键
     */
    @TableId("F_RuleSeedId")
    private String ruleSeedId;

    /**
     * 编码规则主键
     */
    @TableField("F_RuleId")
    private String ruleId;

    /**
     * 用户主键
     */
    @TableField("F_UserId")
    private String userId;

    /**
     * 种子值
     */
    @TableField("F_SeedValue")
    private Integer seedValue;

    /**
     * 创建日期
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
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
}
