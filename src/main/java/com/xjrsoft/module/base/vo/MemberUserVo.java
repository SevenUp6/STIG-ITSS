package com.xjrsoft.module.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class MemberUserVo {

    @JsonProperty("F_UserId")
    private String userId;

    @JsonProperty("F_Account")
    private String account;

    @JsonProperty("F_DepartmentId")
    private String departmentId;

    @JsonProperty("F_DepartmentName")
    private String departmentName;

    @JsonProperty("F_RealName")
    private String realName;

    public String getDingTalkId() {
        return dingTalkId;
    }

    public void setDingTalkId(String dingTalkId) {
        this.dingTalkId = dingTalkId;
    }

    @JsonProperty("F_DingTalkId")
    private String dingTalkId;

    @JsonProperty("F_Gender")
    private Integer gender;

    @JsonIgnore
    private List<DepartmentSimpleVo> departmentSimpleVoList;

    protected void buildDepartmentParams() {
        StringBuilder departmentIdSb = new StringBuilder();
        StringBuilder departmentNameSb = new StringBuilder();
        if (CollectionUtil.isNotEmpty(this.departmentSimpleVoList)) {
            for (DepartmentSimpleVo departmentSimpleVo : this.departmentSimpleVoList) {
                if (departmentIdSb.length() > 0) {
                    departmentIdSb.append(StringPool.COMMA);
                }
                if (departmentNameSb.length() > 0) {
                    departmentNameSb.append(StringPool.COMMA);
                }
                String departmentId = departmentSimpleVo.getDepartmentId();
                String departmentName = departmentSimpleVo.getDepartmentName();
                if (!StringUtil.isEmpty(departmentId)) departmentIdSb.append(departmentId);
                if (!StringUtil.isEmpty(departmentName)) departmentNameSb.append(departmentName);
            }
            this.departmentId = departmentIdSb.toString();
            this.departmentName = departmentNameSb.toString();
        }
    }

    public String getDepartmentId() {
        if (StringUtils.isEmpty(this.departmentId)) {
            buildDepartmentParams();
        }
        return this.departmentId;
    }

    public String getDepartmentName() {
        if (StringUtils.isEmpty(this.departmentName)) {
            buildDepartmentParams();
        }
        return this.departmentName;
    }
}
