package com.xjrsoft.module.itss.tissUtils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.AjaxResult;
import com.xjrsoft.common.utils.StringUtils;
import com.xjrsoft.core.tool.utils.DateUtil;
import com.xjrsoft.core.tool.utils.IoUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.module.base.entity.XjrBaseAnnexesFile;
import com.xjrsoft.module.base.service.IXjrBaseAnnexesFileService;
import com.xjrsoft.module.upload.UploadProperties;
import com.xjrsoft.module.upload.vo.UploadFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping("/updown")
@Api(value = "上传下载工具类", tags = "上传下载工具类接口")
public class UploadAndDownloadUtil {

    @Autowired
    private UploadProperties uploadProperties;
    private final IXjrBaseAnnexesFileService annexesFileService;


    @GetMapping("/getQRCode")
    public AjaxResult getWebQR() {
        AjaxResult ajax = AjaxResult.success();
        String url = "http://8.142.92.214:2828/annexes-files/001APK";
        BufferedImage image = QRCodeUtil.createImage("utf-8", url, 300, 300);
        QRCodeUtil.addUpFont(image, "光华科技运维平台安卓APP下载");
        String imagePath = null;
        String path = null;
        File file = new File("C:\\WorkSpace\\temp\\static\\uploadFiles\\qrcode.JPEG");
        try {

            if (!file.isDirectory()) {
                file.mkdirs();
            }
            ImageIO.write(image, "JPEG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ajax.put("msg","获取二维码成功");
        ajax.put("imagePath",file.getPath());
        return ajax;
    }


    @PostMapping("/apkupload")
    @ApiOperation(value = "上传app升级包")
    public Response<UploadFileVo> uploadFiles(@RequestParam(value = "file", required = true) MultipartFile[] multipartFiles) throws IOException {
        UploadFileVo uploadFileVo = new UploadFileVo();
        String folderId = "001apk";  //app相关都是001apk
        String fileId = "001APK";
        if (uploadFile(multipartFiles[0], folderId, fileId,"0")) {
        uploadFileVo.setFolderId(folderId);
        uploadFileVo.getFileIdList().add(fileId);
        uploadFileVo.getFileUrlList().add("/annexes-files/" + fileId);
        return Response.ok(uploadFileVo);
         }
        return Response.ok(uploadFileVo);
    }

private boolean uploadFile(MultipartFile multipartFile, String folderId, String fileId,String status) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(filename, StringPool.DOT);
        String monthFormat = DateUtil.format(new Date(), "yyyyMM");

        String path = uploadProperties.getPath();
        Integer maxFileSize = Integer.parseInt(uploadProperties.getMaxFileSize())*1024; //获取的是kb，转换为byte

        if(!"apk".equals(suffix)){
//            repairOrderService.removeByIds(Func.toStrList(folderId));
        throw new IOException("文件上传类型错误！");
        }
        // 保存文件到文件夹
        String fileDirPath = File.separator + "static" + File.separator + "uploadFiles" + File.separator + "APK";
        File fileDir = new File(path + fileDirPath);
        if (!fileDir.exists()){
        fileDir.mkdirs();
        }
        File uploadFile = IoUtil.toFile(multipartFile);
        String filePath = fileDirPath + File.separator + DateUtil.format(new Date(),"yyyyMMddHH-mm-sss") + StringPool.DOT + suffix;
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
        annexesFile.setCreateDate(LocalDateTime.now());

        isSuccess = isSuccess && annexesFileService.updateById(annexesFile);
        }
        return isSuccess;
        }
}
