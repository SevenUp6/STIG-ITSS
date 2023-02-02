package com.xjrsoft.module.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * 文件关联关系表实体类
 *
 * @author jobob
 * @since 2020-12-22
 */
@Data
@TableName("xjr_base_annexesfile")
public class XjrBaseAnnexesFile implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 文件主键
	*/
	@TableId("F_Id")
	private String id;
	/**
	* 附件夹主键
	*/
	@TableField("F_FolderId")
	private String folderId;
	/**
	* 文件名称
	*/
	@TableField("F_FileName")
	private String fileName;
	/**
	* 文件路径
	*/
	@TableField("F_FilePath")
	private String filePath;
	/**
	* 文件大小
	*/
	@TableField("F_FileSize")
	private String fileSize;
	/**
	* 文件后缀
	*/
	@TableField("F_FileExtensions")
	private String fileExtensions;
	/**
	* 文件类型
	*/
	@TableField("F_FileType")
	private String fileType;
	/**
	* 下载次数
	*/
	@TableField("F_DownloadCount")
	private Integer downloadCount;
	/**
	* 创建日期
	*/
	@TableField(value = "F_CreateDate", fill = FieldFill.INSERT)
	private LocalDateTime createDate;
	/**
	* 创建用户主键
	*/
	@TableField(value = "F_CreateUserId", fill = FieldFill.INSERT)
	private String createUserId;
	/**
	* 创建用户
	*/
	@TableField(value = "F_CreateUserName", fill = FieldFill.INSERT)
	private String createUserName;
	/**
	* 绑定工单的状态
	*/
	@TableField("F_FileStatus")
	private String fileStatus;

}
