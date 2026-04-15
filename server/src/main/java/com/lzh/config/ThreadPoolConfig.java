package com.lzh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ThreadPoolConfig {

    @Bean("booklistExecutor")
    public Executor booklistExecutor() {
        // 获取 CPU 核心数
        int cpuCore = Runtime.getRuntime().availableProcessors();

        return new ThreadPoolExecutor(
                cpuCore,                      // 核心线程数
                cpuCore * 2,                  // 最大线程数
                60L,                          // 空闲线程存活时间
                TimeUnit.SECONDS,             // 时间单位
                new LinkedBlockingQueue<>(5000), // 有界队列，防止 OOM
                new ThreadFactory() {         // 自定义线程工厂，方便监控和排查问题
                    private final AtomicInteger threadNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "booklist-async-thread-" + threadNumber.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：由调用者线程处理
        );
    }
}
