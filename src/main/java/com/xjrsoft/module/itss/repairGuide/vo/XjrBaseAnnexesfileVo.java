package com.xjrsoft.module.itss.repairGuide.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * 视图实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
@ApiModel(value = "XjrBaseAnnexesfileVO对象", description = "XjrBaseAnnexesfileVO对象")
public class XjrBaseAnnexesfileVo {
	private static final long serialVersionUID = 1L;

	private String fId;

	private String url;

	private String fFilename;
//	private String fFilesize;
//	private String fFileextensions;
//	private String fFiletype;
//	private Integer fDownloadcount;
//	private LocalDateTime fCreatedate;
//	private String fCreateuserid;

//	private String fCreateusername;
	/**
	 * 绑定工单的状态
	 */

//	private Integer fFilestatus;
}
