package com.xjrsoft.module.buildCode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AppCodeSchemaDto {

    @JsonProperty("stepOne")
    private AppBaseInfoDto appBaseInfoDto;

    @JsonProperty("stepTwo")
    private DbSettingDto dbSettingDto;

    @JsonProperty("stepThree")
    private List<AppFieldConfigDto> formDataDtoList;

    @JsonProperty("stepFour")
    private List<AppQueryFieldDto> queryFieldDtoList;

    @JsonProperty("stepFive")
    private AppListViewSettingDto listViewSettingDto;

    @JsonProperty("stepSix")
    private BaseInfoDto baseInfoDto;

    @JsonProperty("stepSeven")
    private AppModuleDataDto appModuleDataDto;

    @JsonProperty("codeContent")
    private AppCodeContentDto appCodeContentDto;

    @JsonProperty("stepEight")
    private AppModuleInfoDto appModuleInfoDto;
}
