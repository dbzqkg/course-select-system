package com.lzh.server.proxytest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SpringBootTest
public class test {
    @Test
    public void test() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // --- 核心步骤：开启保存代理类文件的开关 ---
        // 如果你用的是 JDK 8：
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        // 如果你用的是 JDK 9 及以上版本（如 JDK 17/21）：
        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

        RealStar jay = new RealStar();


        // 生成代理对象
        // 注意：这里传了两个接口 CanSing.class, CanDance.class
        Object proxyInstance = Proxy.newProxyInstance(
                jay.getClass().getClassLoader(),
                new Class[]{CanSing.class, CanDance.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("[经纪人] 谈合同，收定金...");
                        Object result = method.invoke(jay, args); // 执行真实对象的方法
                        System.out.println("[经纪人] 演出结束，结清尾款。");
                        return result;
                    }
                }
        );

    }
}
