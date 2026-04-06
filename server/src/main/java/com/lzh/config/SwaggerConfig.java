package com.lzh.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration // 【1】告诉 Spring Boot：老弟，这是一个配置类，启动的时候记得来读一下！
public class SwaggerConfig {

    @Bean // 【2】告诉 Spring Boot：帮我把这个方法返回的 OpenAPI 对象，塞到你的容器里去。
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 【3】封面定制：给咱们的接口文档网页写个大标题和版本号
                .info(new Info()
                        .title("西电选课系统 API")
                        .version("1.0"))

                // ==========================================
                // 下面这两大坨，就是专门为咱们的 JWT 拦截器定制的“全局万能钥匙”！
                // ==========================================

                // 【4】声明一把全局的锁：
                // 这句话的意思是：告诉 Swagger，我这个项目里所有的接口，都需要一把名叫 "Bearer Token" 的钥匙才能进！
                // 加上这行代码，网页上所有的接口旁边都会多出一个“小锁 🔒的图标。
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))

                // 【5】定义这把钥匙的形状（制造图纸）：
                .components(new Components()
                        // 给名叫 "Bearer Token" 的钥匙下定义：
                        .addSecuritySchemes("Bearer Token", new SecurityScheme()
                                // 形状1：它是 HTTP 协议的标准认证方式
                                .type(SecurityScheme.Type.HTTP)

                                // 形状2（核心！）：它的前缀必须叫 "bearer"
                                // 👉 也就是因为配置了这个，你在网页里粘贴 Token 的时候，才不需要手动敲 "Bearer " 这7个字符！Swagger 框架在发请求时，会在底层自动帮你拼接上去！
                                .scheme("bearer")

                                // 形状3：备注一下，咱们装的货是 "JWT" 格式的
                                .bearerFormat("JWT")));
    }
}