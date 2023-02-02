package com.xjrsoft.module.itss.machineCompany.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * 设备所属单位表实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
@TableName("machine_company")
public class MachineCompany implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 公司id
	*/
	@TableId("id")
	private String id;
	/**
	* 公司编码
	*/
	@TableField("com_code")
	private String comCode;
	/**
	* 公司名称
	*/
	@TableField("com_name")
	private String comName;
	/**
	* 父级id
	*/
	@TableField("pcode")
	private String pcode;
	/**
	* 父级名称
	*/
	@TableField("pname")
	private String pname;
	/**
	* 节点
	*/
	@TableField("node")
	private String node;
	/**
	* 节点名称
	*/
	@TableField("node_name")
	private String nodeName;
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
