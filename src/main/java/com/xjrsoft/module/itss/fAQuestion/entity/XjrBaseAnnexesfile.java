package com.xjrsoft.module.itss.fAQuestion.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * 实体类
 *
 * @author hanhe
 * @since 2022-10-20
 */
@Data
@TableName("xjr_base_annexesfile")
public class XjrBaseAnnexesfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId("F_Id")
	private String fId;
	@TableField("F_FolderId")
	private String fFolderid;
	@TableField("F_FileName")
	private String fFilename;
	@TableField("F_FilePath")
	private String fFilepath;
	@TableField("F_FileSize")
	private String fFilesize;
	@TableField("F_FileExtensions")
	private String fFileextensions;
	@TableField("F_FileType")
	private String fFiletype;
	@TableField("F_DownloadCount")
	private Integer fDownloadcount;
	@TableField("F_CreateDate")
	private LocalDateTime fCreatedate;
	@TableField("F_CreateUserId")
	private String fCreateuserid;
	@TableField("F_CreateUserName")
	private String fCreateusername;
	/**
	* 绑定工单的状态
	*/
	@TableField("F_FileStatus")
	private Integer fFilestatus;


}
