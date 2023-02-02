package com.xjrsoft.module.itss.fAQuestion.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * 常见问题表实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
@TableName("f_a_question")
public class FAQuestion implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 问题id
	*/
	@TableId("id")
	private String id;
	/**
	* 问题描述
	*/
	@TableField("problem_des")
	private String problemDes;
	/**
	* 所属设备模块id
	*/
	@TableField("mod_id")
	private String modId;
	/**
	* 所属设备模块名称
	*/
	@TableField("mod_name")
	private String modName;
	@TableField("fault_id")
	private String faultId;
	@TableField("fault_name")
	private String faultName;
	/**
	* 处理建议
	*/
	@TableField("suggestion")
	private String suggestion;
	/**
	* 备注
	*/
	@TableField("remark")
	private String remark;
	/**
	* 创建人
	*/
	@TableField("created_by")
	private String createdBy;
	/**
	* 创建人
	*/
	@TableField("created_name")
	private String createdName;
	/**
	* 创建时间
	*/
	@TableField("created_time")
	private LocalDateTime createdTime;
	/**
	* 更新人
	*/
	@TableField("updated_by")
	private String updatedBy;
	/**
	* 更新时间
	*/
	@TableField("updated_time")
	private LocalDateTime updatedTime;


}
