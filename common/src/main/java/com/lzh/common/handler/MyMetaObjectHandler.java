package com.lzh.common.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 只要执行 insert，自动把当前时间塞进去！
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 注意：因为现在是注册，用户还没登录，没有 Token。
        // 所以 V1.0 阶段，如果是系统自己注册，我们暂时默认填个 "SYSTEM" 或前端传来的学号。
        // 等你以后做了拦截器，这里可以直接从 ThreadLocal 里拿当前登录人的 ID！
        this.strictInsertFill(metaObject, "createBy", String.class, "SYSTEM");
        this.strictInsertFill(metaObject, "updateBy", String.class, "SYSTEM");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 只要执行 update，自动更新修改时间！
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateBy", String.class, "SYSTEM");
    }
}
