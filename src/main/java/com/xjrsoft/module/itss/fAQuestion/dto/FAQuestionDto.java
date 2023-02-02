package com.xjrsoft.module.itss.fAQuestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 常见问题表数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
public class FAQuestionDto {
	private static final long serialVersionUID = 1L;

	@JsonProperty("problem_des")
	private String problemDes;

	@JsonProperty("mod_name")
	private String modName;

	@JsonProperty("mod_id")
	private String modId;

	@JsonProperty("fault_name")
	private String faultName;

	@JsonProperty("fault_id")
	private String faultId;

	@JsonProperty("suggestion")
	private String suggestion;

}
