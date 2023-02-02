package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_base_tempfield")
public class XjrBaseTempfield {
    /**
     * 公司主键
     */
    @TableId("F_Id")
    private String id;

    @TableField("F_Key")
    private String fkey;

    @TableField("F_Type")
    private String type;

    @TableField("F_Value")
    private String fvalue;

    @TableField("F_CreateTime")
    private Date createTime;

}
