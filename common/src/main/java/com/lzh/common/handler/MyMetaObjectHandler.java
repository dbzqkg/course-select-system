package com.lzh.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lzh.common.context.UserContext;
import com.lzh.common.util.ThreadLocalUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 自动填充时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 2. 动态填充操作人
        UserContext user = ThreadLocalUtil.get();
        String operator = (user != null) ? user.stuId() : "SYSTEM"; // 如果没登录（如注册场景），填 SYSTEM

        this.strictInsertFill(metaObject, "createBy", String.class, operator);
        this.strictInsertFill(metaObject, "updateBy", String.class, operator);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        UserContext user = ThreadLocalUtil.get();
        String operator = (user != null) ? user.stuId() : "SYSTEM";
        this.strictUpdateFill(metaObject, "updateBy", String.class, operator);
    }
}