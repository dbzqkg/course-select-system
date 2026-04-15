package com.lzh.common.handler;

import com.lzh.common.exception.BusinessException;
import com.lzh.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器（老大的兜底防线）
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 专门拦截你刚才抛出的 BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException ex) {
        log.warn("触发业务异常: {}", ex.getMessage());
        // 这里调用我们下面改造后的 error 方法，传入自定义 code
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 终极兜底：拦截其他所有未知的报错（比如空指针、数据库崩了）
     * 绝对不能把一堆英文报错栈直接暴露给前端，太 low 了
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error("系统未知异常: ", ex);
        return Result.error("服务器开小差了，请联系管理员");
    }

}