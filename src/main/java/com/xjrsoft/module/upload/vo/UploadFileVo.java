package com.xjrsoft.module.upload.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UploadFileVo {

    @JsonProperty("FolderId")
    private String folderId;

    @JsonProperty("FileIds")
    private List<String> fileIdList;

    @JsonProperty("FileUrl")
    private List<String> fileUrlList;

    public List<String> getFileIdList(){
        if (fileIdList == null) {
            fileIdList = new ArrayList<>();
        }
        return fileIdList;
    }

    public List<String> getFileUrlList(){
        if (fileUrlList == null) {
            fileUrlList = new ArrayList<>();
        }
        return fileUrlList;
    }
}
