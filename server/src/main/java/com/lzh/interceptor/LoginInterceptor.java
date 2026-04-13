package com.lzh.interceptor;

import com.lzh.common.context.UserContext;
import com.lzh.common.util.JwtUtil;
import com.lzh.common.util.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import com.lzh.common.constants.RedisConstants.*;

import java.util.concurrent.TimeUnit;

import static com.lzh.common.constants.RedisConstants.STUDENT_SCHEDULE_BIT;
import static com.lzh.common.util.JwtUtil.EXPIRATION_TIME;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate stringRedisTemplate; // 依然可以用它，但拿位图要用 execute

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ... (省略 OPTIONS 和 Token 非空校验逻辑，保持不变) ...

        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!StringUtils.hasText(token)) {
            response.setStatus(401);
            return false;
        }

        try {
            Claims claims = JwtUtil.parseToken(token);

            Long id = Long.valueOf(claims.get("id").toString());
            String stuId = claims.get("stuId", String.class);
            String name = claims.get("name", String.class);

            // ============= 核心改动点：拉取 1024 位图 =============
            // 使用 execute 绕过 StringRedisTemplate 的序列化器，直接拿原始字节

            byte[] scheduleBitmap = stringRedisTemplate.execute((RedisCallback<byte[]>) connection ->
                    connection.get((STUDENT_SCHEDULE_BIT + stuId).getBytes())
            );

            // 如果 Redis 里暂时没有（比如预热漏了），可以根据业务决定是报错还是给个空位图
            if (scheduleBitmap == null) {
                log.warn("学生 {} 的位图在 Redis 中缺失，请检查预热逻辑！", stuId);
                scheduleBitmap = new byte[128]; // 兜底给一个全 0 位图（128字节=1024位）
            }

            // 封装完整的 UserContext (现在是 4 个参数了)
            UserContext userContext = new UserContext(id, stuId, name, scheduleBitmap);
            ThreadLocalUtil.set(userContext);
            // ===================================================

            // ... (下面原有的 Token 刷新逻辑，保持不变) ...
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            long remainingTime = expirationTime - currentTime;

            if (remainingTime < (EXPIRATION_TIME / 2)) {
                String lockKey = "lock:refresh:" + stuId;
                Boolean getLock = stringRedisTemplate.opsForValue()
                        .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

                if (Boolean.TRUE.equals(getLock)) {
                    log.info("为学号: {} 签发新 Token", stuId);
                    String newToken = JwtUtil.createToken(id, stuId, name);
                    response.setHeader("New-Token", newToken);
                    response.setHeader("Access-Control-Expose-Headers", "New-Token");
                }
            }

            return true;
        } catch (Exception e) {
            log.error("Token 校验失败: {}", e.getMessage());
            response.setStatus(401);
            return false;
        }
    }

    // ... afterCompletion 保持不变 ...
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove(); // 必须防内存泄漏
    }
}