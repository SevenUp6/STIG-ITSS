package com.xjrsoft.module.itss.fAQuestion.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
}
