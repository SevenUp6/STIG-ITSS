package com.xjrsoft.module.itss.statistics.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备模块表实体类
 *
 * @author HANHE
 * @since 2022-10-12
 */
@Data
@TableName("machine_module")
public class MachineModule implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 设备模块id
	*/
	@TableId("id")
	private String id;
	/**
	* 设备模块名称
	*/
	@TableField("mod_name")
	private String modName;
	/**
	* 设备类型id
	*/
	@TableField("type_id")
	private String typeId;
	/**
	* 设备类型名称
	*/
	@TableField("type_name")
	private String typeName;
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
