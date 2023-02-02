package com.xjrsoft.module.base.dto;

import lombok.Data;

@Data
public class WeChatDepartDto {
    //部门id
    private Integer id;


    //部门名字
    private String name;

    
    //英文名称
    private String name_en;

    //父部门id
    private Integer parentid;

    //排序
    private Integer order;
    
    
}
