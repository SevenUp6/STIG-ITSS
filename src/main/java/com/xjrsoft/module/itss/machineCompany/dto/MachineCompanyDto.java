package com.xjrsoft.module.itss.machineCompany.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.xjrsoft.module.itss.machineCompany.entity.MachineCompany;
import com.xjrsoft.module.itss.machineType.dto.MachineModuleDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备所属单位表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-13
 */
@Data
public class  MachineCompanyDto {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("comCode")
	private String comCode;

	@JsonProperty("comName")
	private String comName;

	@JsonProperty("pcode")
	private String pcode;

	@JsonProperty("pname")
	private String pname;

	@JsonProperty("nodeName")
	private String nodeName;

	@JsonProperty("companies")
	private List<MachineCompanyDto> companies;

}
