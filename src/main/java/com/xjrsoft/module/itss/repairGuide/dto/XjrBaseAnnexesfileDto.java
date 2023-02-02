package com.xjrsoft.module.itss.repairGuide.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 数据传输对象实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
public class XjrBaseAnnexesfileDto {
	private static final long serialVersionUID = 1L;
	@JsonProperty("fid")
	private String fId;

	@JsonProperty("url")
	private String url;

	@JsonProperty("ffileName")
	private String fFilename;
}
