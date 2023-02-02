package com.xjrsoft.module.upload.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.DateUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.core.tool.utils.IoUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.entity.XjrBaseAnnexesFile;
import com.xjrsoft.module.base.service.IXjrBaseAnnexesFileService;
import com.xjrsoft.module.itss.fAQuestion.service.IFAQuestionService;
import com.xjrsoft.module.itss.repairGuide.service.IRepairGuideService;
import com.xjrsoft.module.itss.repairOrder.service.IRepairOrderService;
import com.xjrsoft.module.upload.UploadProperties;
import com.xjrsoft.module.upload.vo.UploadFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 任务调度模板基础信息表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-12-23
 */
@RestController
@RequestMapping("/upload")
@AllArgsConstructor
@Api(value = "/upload", tags = "文件上传模块")
public class UploadController {

    private final IXjrBaseAnnexesFileService annexesFileService;
    @Autowired
    private final IRepairOrderService repairOrderService;

    @Autowired
    private final IRepairGuideService repairGuideService;
    @Autowired
    private final IFAQuestionService fAQuestionService;
    ;
    @Autowired
    private UploadProperties uploadProperties;


//    @PostMapping
//    @ApiOperation(value = "单文件上传")
//    public Response<UploadFileVo> uploadFile(@RequestParam(value = "file", required = true) MultipartFile multipartFile) throws IOException {
//        String folderId = IdWorker.get32UUID();
//        String fileId = IdWorker.get32UUID();
//        if (uploadFile(multipartFile, folderId, fileId)) {
//            UploadFileVo uploadFileVo = new UploadFileVo();
//            uploadFileVo.setFolderId(folderId);
//            uploadFileVo.getFileIdList().add(fileId);
//            uploadFileVo.getFileUrlList().add("/annexes-files/" + fileId);
//            return Response.ok(uploadFileVo);
//        }
//        return Response.notOk();
//    }

//    @PostMapping("/{folderId}")
//    @ApiOperation(value = "单文件上传")
//    public Response<UploadFileVo> uploadFileToFolder(@PathVariable String folderId, @RequestParam(value = "file", required = true) MultipartFile multipartFile) throws IOException {
//        String fileId = IdWorker.get32UUID();
//        if (uploadFile(multipartFile, folderId, fileId)) {
//            UploadFileVo uploadFileVo = new UploadFileVo();
//            uploadFileVo.setFolderId(folderId);
//            uploadFileVo.getFileIdList().add(fileId);
//            uploadFileVo.getFileUrlList().add("/annexes-files/" + fileId);
//            return Response.ok(uploadFileVo);
//        }
//        return Response.notOk();
//    }


//    @PostMapping("/multiple-file-upload/{folderId}")
//    @ApiOperation(value = "多文件上传")
//    public Response<UploadFileVo> uploadFilesToFolder(@PathVariable String folderId, @RequestParam(value = "file", required = true) MultipartFile[] multipartFiles) throws IOException {
//        UploadFileVo uploadFileVo = new UploadFileVo();
//        if (multipartFiles != null && multipartFiles.length > 0) {
//            uploadFileVo.setFolderId(folderId);
//            for (MultipartFile multipartFile : multipartFiles) {
//                String fileId = IdWorker.get32UUID();
//                if (uploadFile(multipartFile, folderId, fileId)) {
//                    uploadFileVo.getFileIdList().add(fileId);
//                    uploadFileVo.getFileUrlList().add("/annexes-files/" + fileId);
//                }
//            }
//
//        }
//        return Response.ok(uploadFileVo);
//    }

@PostMapping("/multiple-file-upload")
@ApiOperation(value = "多文件上传")
public Response<UploadFileVo> uploadFiles(@RequestParam(value = "file", required = true) MultipartFile[] multipartFiles,
                                          @RequestParam(value = "id", required = true)String id,
                                          @RequestParam(value = "status", required = true)String status) throws IOException {
    UploadFileVo uploadFileVo = new UploadFileVo();
//    String folderId = IdWorker.get32UUID();
    String folderId = id;  //存工单的id
    if (multipartFiles != null && multipartFiles.length > 1) { //上传多个文件
        uploadFileVo.setFolderId(folderId);
        for (MultipartFile multipartFile : multipartFiles) {
            String fileId = IdWorker.get32UUID();
            if (uploadFile(multipartFile, folderId, fileId,status)) {
                uploadFileVo.getFileIdList().add(fileId);
                uploadFileVo.getFileUrlList().add("/annexes-files/" + fileId);
            }
        }

    }else if ( multipartFiles.length==0){//编辑的时候没有新上传文件
        return Response.ok(uploadFileVo);
    }else{   //只上传一个文件
        String fileId = IdWorker.get32UUID();
        if (uploadFile(multipartFiles[0], folderId, fileId,status)) {
            uploadFileVo.setFolderId(folderId);
            uploadFileVo.getFileIdList().add(fileId);
            uploadFileVo.getFileUrlList().add("/annexes-files/" + fileId);
            return Response.ok(uploadFileVo);
        }
    }
    return Response.ok(uploadFileVo);
}

    private boolean uploadFile(MultipartFile multipartFile, String folderId, String fileId,String status) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(filename, StringPool.DOT);
        String monthFormat = DateUtil.format(new Date(), "yyyyMM");

        String path = uploadProperties.getPath();
        Integer maxFileSize = Integer.parseInt(uploadProperties.getMaxFileSize())*1024; //获取的是kb，转换为byte

        if(!uploadProperties.getAllowTypes().contains(suffix)){
//            repairOrderService.removeByIds(Func.toStrList(folderId));
            throw new IOException("文件上传类型错误！");
        }
        if(maxFileSize<multipartFile.getSize()){
//            repairOrderService.removeByIds(Func.toStrList(folderId));
//            fAQuestionService.removeByIds(Func.toStrList(folderId));
//            repairGuideService.removeByIds(Func.toStrList(folderId));
            throw new IOException("文件大小超出限制！");
        }

        // 保存文件到文件夹
        String fileDirPath = File.separator + "static" + File.separator + "uploadFiles" + File.separator + monthFormat;
        File fileDir = new File(path + fileDirPath);
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File uploadFile = IoUtil.toFile(multipartFile);
        String filePath = fileDirPath + File.separator + IdWorker.get32UUID() + StringPool.DOT + suffix;
        File file = new File(path + filePath);
        boolean isSuccess = uploadFile.renameTo(file);
        if (isSuccess) {
            // 保存数据库记录
            XjrBaseAnnexesFile annexesFile = new XjrBaseAnnexesFile();
            annexesFile.setId(fileId);
            annexesFile.setFolderId(folderId);
            annexesFile.setFileName(filename);
            annexesFile.setFilePath(filePath);
            annexesFile.setFileSize(String.valueOf(multipartFile.getSize()));
            annexesFile.setFileExtensions(StringPool.DOT + suffix);
            annexesFile.setFileType(suffix);
            annexesFile.setFileStatus(status);

            isSuccess = isSuccess && annexesFileService.save(annexesFile);
        }
        return isSuccess;
    }
}
