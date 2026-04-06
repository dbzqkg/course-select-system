package com.lzh.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT 令牌工具类 (大厂企业级 Base64 规范版)
 */
public class JwtUtil {
    public static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;
    private static final String BASE64_SECRET = "WGlEaWFuVW5pdmVyc2l0eUNvdXJzZVNlbGVjdFN5c3RlbTIwMjZCYXNlNjRY";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(BASE64_SECRET));

    public static String createToken(Long id, String stuId, String name) {
        return Jwts.builder()
                .setClaims(Map.of("id", id, "stuId", stuId, "name", name))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}