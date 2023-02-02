package com.xjrsoft.module.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品关系表
 * </p>
 *
 * @author jobob
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_itemrelation")
public class XjrBaseItemRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("F_Id")
    private String id;

    /**
     * 商品主键
     */
    @TableField("F_ItemId")
    private String itemId;

    /**
     * 订单主键
     */
    @TableField("F_OrderId")
    private String orderId;

    /**
     * 物料主键
     */
    @TableField("F_MaterialId")
    private String materialId;

    /**
     * 类型，1-商品对应关系，2-物料对应关系
     */
    @TableField("F_Category")
    private Integer category;

    /**
     * 数量
     */
    @TableField("F_Count")
    private Integer count;

    /**
     * 创建时间
     */
    @TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
    private String createDate;

    /**
     * 创建人主键值
     */
    @TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
    private String createUserId;

    /**
     * 创建人名称
     */
    @TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
    private String createUserName;
}
