package com.xjrsoft.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xjrsoft.common.allenum.DeleteMarkEnum;
import com.xjrsoft.common.allenum.EnabledMarkEnum;
import com.xjrsoft.core.secure.utils.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * @title 数据审计处理器
 * @desc  用于新增或者更新  自动插入 相应字段
 * @author tzx
 * @create 2020年11月9日 11:30:59
 * */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * @title 新增自动填充
     * @desc  fieldName 使用实体类字段名 而不是数据库字段名
     * */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");

        //默认插入创建人Id
        this.strictInsertFill(metaObject, "createUserId", String.class,  SecureUtil.getUserId()); // 起始版本 3.3.0(推荐使用)
        //默认插入创建人名称
        this.strictInsertFill(metaObject, "createUserName", String.class,  SecureUtil.getUserName());
        //默认插入创建时间
        this.strictInsertFill(metaObject, "createDate", LocalDateTime.class, LocalDateTime.now());
        //默认插入未删除
        this.strictInsertFill(metaObject, "deleteMark", Integer.class, DeleteMarkEnum.NODELETE.getCode());
        //默认插入已启用
        this.strictInsertFill(metaObject, "enabledMark", Integer.class, EnabledMarkEnum.ENABLED.getCode());
        //默认插入为主系统
        this.strictInsertFill(metaObject, "isSubSystem", Integer.class, 0);
    }


    /**
     * @title 修改自动填充
     * @desc  fieldName 使用实体类字段名 而不是数据库字段名
     * */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        //默认插入修改人Id
        this.strictUpdateFill(metaObject, "modifyUserId", String.class,  SecureUtil.getUserId()); // 起始版本 3.3.0(推荐使用)
        //默认插入修改人名称
        this.strictUpdateFill(metaObject, "modifyUserName", String.class,  SecureUtil.getUserName());
        //默认插入修改时间
        this.strictUpdateFill(metaObject, "modifyDate", LocalDateTime.class, LocalDateTime.now());
    }
}
