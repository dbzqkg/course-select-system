package com.lzh.common.util;

import com.lzh.common.context.UserContext;

/**
 * 全局上下文透传工具
 */
public class ThreadLocalUtil {

    private ThreadLocalUtil() {}

    private static final ThreadLocal<UserContext> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(UserContext user) {
        THREAD_LOCAL.set(user);
    }

    public static UserContext get() {
        return THREAD_LOCAL.get();
    }

    public static String getStuId() {
        UserContext user = get();
        return user != null ? user.stuId() : null;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}