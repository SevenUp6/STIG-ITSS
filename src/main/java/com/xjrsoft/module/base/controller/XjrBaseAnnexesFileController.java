package com.xjrsoft.module.base.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.*;
import com.xjrsoft.module.base.entity.XjrBaseAnnexesFile;
import com.xjrsoft.module.base.service.IXjrBaseAnnexesFileService;
import com.xjrsoft.module.base.vo.AnnexesFileVo;
import com.xjrsoft.module.upload.UploadProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 文件关联关系表 控制器
 *
 * @author jobob
 * @since 2020-12-22
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/annexes-files")
@Api(value = "文件关联关系表", tags = "文件关联关系表接口")
public class XjrBaseAnnexesFileController {

	private final IXjrBaseAnnexesFileService annexesFileService;

	private final ObjectMapper objectMapper;

	@Autowired
	private UploadProperties uploadProperties;

	@GetMapping("/url/{folderId}")
	@ApiOperation(value = "根据文件夹Id获取图片地址")
	public Response<List<AnnexesFileVo>> getFileUrlByFolderId(@PathVariable String folderId,@RequestParam(required=false) String status) {
		List<XjrBaseAnnexesFile> annexesFileList = annexesFileService.list(Wrappers.<XjrBaseAnnexesFile>query().lambda()
				.eq(XjrBaseAnnexesFile::getFolderId, folderId)
				.eq(!StringUtil.isEmpty(status),XjrBaseAnnexesFile::getFileStatus, status));
		List<AnnexesFileVo> annexesFileVoList = BeanUtil.copyList(annexesFileList, AnnexesFileVo.class);
		if (CollectionUtil.isNotEmpty(annexesFileList)) {
			String getFileApiUrlPrefix = "/annexes-files/";
			for (AnnexesFileVo annexesFileVo : annexesFileVoList) {
				annexesFileVo.setUrl(getFileApiUrlPrefix + annexesFileVo.getId());
			}
		}
		return Response.ok(annexesFileVoList);
	}


	public  List<AnnexesFileVo> getfileUrlByFolderId( String folderId,  String status) {
		List<XjrBaseAnnexesFile> annexesFileList = annexesFileService.list(Wrappers.<XjrBaseAnnexesFile>query().lambda()
				.eq(XjrBaseAnnexesFile::getFolderId, folderId)
				.eq(!StringUtil.isEmpty(status),XjrBaseAnnexesFile::getFileStatus, status));
		List<AnnexesFileVo> annexesFileVoList = BeanUtil.copyList(annexesFileList, AnnexesFileVo.class);
		if (CollectionUtil.isNotEmpty(annexesFileList)) {
			String getFileApiUrlPrefix = "/annexes-files/";
			for (AnnexesFileVo annexesFileVo : annexesFileVoList) {
				annexesFileVo.setUrl(getFileApiUrlPrefix + annexesFileVo.getId());
			}
		}
		return annexesFileVoList;
	}

	@GetMapping("/{fileId}")
	@ApiOperation(value = "根据文件夹Id获取图片地址")
	public void getFileById(@PathVariable String fileId) throws IOException {
 		XjrBaseAnnexesFile annexesFile = annexesFileService.getById(fileId);
		File file = null;
		if (annexesFile != null) {
			String filePath = annexesFile.getFilePath();
			file = new File(filePath);
			if (!file.exists()) {
				String projectPath = uploadProperties.getPath();
//				String projectPath = IoUtil.getProjectPath();
				file = FileUtil.file(projectPath + filePath);
			}
		}

		HttpServletResponse response = WebUtil.getResponse();
		if (file == null) {
			try {
				String result = objectMapper.writeValueAsString(Response.notOk("文件不存在！"));
				response.getOutputStream().write(result.getBytes());
				return;
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}else {
			// 设置下载文件名
			WebUtil.writeFileToResponse(annexesFile.getFileName(), file);
		}
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "根据文件夹Id获取图片地址")
	public Response deleteFile(@PathVariable String id) {
		return Response.status(annexesFileService.removeById(id));
	}

	@DeleteMapping("/multi/{folderId}")
	@ApiOperation(value = "根据文件夹Id获取图片地址")
	public Response deleteFolderFiles(@PathVariable String folderId) {
		Wrapper<XjrBaseAnnexesFile> deleteWrapper = Wrappers.<XjrBaseAnnexesFile>query().lambda().eq(XjrBaseAnnexesFile::getFolderId, folderId);
		return Response.status(annexesFileService.remove(deleteWrapper));
	}
}
