package com.xjrsoft.module.language.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 语言映照表
 * </p>
 *
 * @author jobob
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xjr_lg_map")
public class XjrLgMap implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("F_Id")
    private String id;

    /**
     * 显示内容
     */
    @TableField("F_Name")
    private String name;

    /**
     * 编码(系统自动产生，作为关联项)
     */
    @TableField("F_Code")
    private String code;

    /**
     * 对应语言显示编码
     */
    @TableField("F_TypeCode")
    private String typeCode;
}
