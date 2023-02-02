package com.xjrsoft.module.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MemberUserIdDto {

    @JsonProperty("roleId")
    private String roleId;

    @JsonProperty("postId")
    private String postId;

    @JsonProperty("departmentId")
    private String departmentId;

    @JsonProperty("userIds")
    private List<String> userIdList;
}
