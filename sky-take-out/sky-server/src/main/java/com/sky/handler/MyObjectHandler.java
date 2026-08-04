package com.sky.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sky.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyObjectHandler implements MetaObjectHandler {
    private static final String FIELD_CREATE_TIME = "createTime";
    private static final String FIELD_UPDATE_TIME = "updateTime";
    private static final String FIELD_CREATE_USER = "createUser";
    private static final String FIELD_UPDATE_USER = "updateUser";

    @Override
    public void insertFill(MetaObject metaObject){
        LocalDateTime currentTime = LocalDateTime.now();
        Long currentUser = getCurrentUser();
        this.strictInsertFill(metaObject,FIELD_CREATE_TIME, LocalDateTime.class,currentTime);
        this.strictInsertFill(metaObject,FIELD_UPDATE_TIME, LocalDateTime.class,currentTime);
        this.strictInsertFill(metaObject,FIELD_CREATE_USER, Long.class,currentUser);
        this.strictInsertFill(metaObject,FIELD_UPDATE_USER, Long.class,currentUser);
    }

    @Override
    public void updateFill(MetaObject metaObject){
        LocalDateTime currentTime = LocalDateTime.now();
        Long currentUser = getCurrentUser();
        this.strictUpdateFill(metaObject,FIELD_UPDATE_TIME, LocalDateTime.class,currentTime);
        this.strictUpdateFill(metaObject,FIELD_UPDATE_USER, Long.class,currentUser);
    }

    private Long getCurrentUser(){
        Long id = BaseContext.getCurrentId();
        log.info("当前操作用户 ID: {}", id);
        return id;
    }
}
