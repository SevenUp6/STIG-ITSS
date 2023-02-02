package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SaveAuthorizeDto {

    @JsonProperty("objectId")
    private String objectId;

    @JsonProperty("objectType")
    private Integer objectType;

    @JsonProperty("itemJson")
    private AuthorizeIdsDto authorizeIdsDto;
}
