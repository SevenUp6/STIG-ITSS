package com.xjrsoft.module.itss.fAQuestion.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * 常见问题表视图实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
@ApiModel(value = "FAQuestionVO对象", description = "常见问题表")
public class FAQuestionVo {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("problem_des")
	private String problemDes;

	@JsonProperty("mod_name")
	private String modName;

	@JsonProperty("mod_id")
	private String modId;

	@JsonProperty("fault_id")
	private String faultId;

	@JsonProperty("fault_name")
	private String faultName;

	@JsonProperty("suggestion")
	private String suggestion;
	/**
	 * 创建人
	 */
	@JsonProperty("created_by")
	private String createdBy;
	/**
	 * 创建时间
	 */
	@JsonProperty("created_time")
	private LocalDateTime createdTime;
	/**
	 * 更新人
	 */
	@JsonProperty("updated_by")
	private String updatedBy;
	/**
	 * 更新时间
	 */
	@JsonProperty("updated_time")
	private LocalDateTime updatedTime;

	@JsonProperty("created_name")
	private String createdName;
}
