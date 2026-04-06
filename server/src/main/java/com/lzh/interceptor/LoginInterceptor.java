package com.lzh.interceptor;

import com.lzh.common.context.UserContext;
import com.lzh.common.util.JwtUtil;
import com.lzh.common.util.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.lzh.common.util.JwtUtil.EXPIRATION_TIME;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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

            // 存入 ThreadLocal
            UserContext userContext = new UserContext(id, stuId, name);
            ThreadLocalUtil.set(userContext);

            // 架构：不足一半寿命时，无感刷新 Token
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            long remainingTime = expirationTime - currentTime;

            // 寿命不足一半 (小于 1 小时)
            if (remainingTime < (EXPIRATION_TIME / 2)) {
                log.info("Token 寿命不足一半，触发无感刷新！学号: {}", stuId);
                // 重新签发满血 Token
                String newToken = JwtUtil.createToken(id, stuId, name);
                // 塞进响应头告诉前端
                response.setHeader("New-Token", newToken);
                // 【史诗级避坑】：必须要加这行跨域暴露，否则前端 axios 根本看不见你自己定义的这个 Header！
                response.setHeader("Access-Control-Expose-Headers", "New-Token");
            }

            return true;
        } catch (Exception e) {
            log.error("Token 校验失败或已彻底过期: {}", e.getMessage());
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove(); // 必须防内存泄漏
    }
}