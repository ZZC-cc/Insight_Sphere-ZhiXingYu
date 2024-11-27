package com.zzc.init.utils;

import com.zzc.init.admin.user.model.entity.User;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "cc1433223"; // 替换成你的密钥

    // 日期格式化器
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 生成 JWT，传入 User 对象
    public static String generateToken(User user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUser_id());
            claims.put("username", user.getUsername());
            claims.put("name", user.getName());
            claims.put("role", user.getRole());
            claims.put("avatar", user.getAvatar());
            claims.put("mobile", user.getMobile());
            claims.put("status", user.getStatus());
            // 将 LocalDateTime 转换为字符串
            claims.put("createTime", user.getCreateTime().format(formatter));
            claims.put("updateTime", user.getUpdateTime().format(formatter));

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2小时
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    // 验证 JWT
    // 验证 JWT，判断是否过期
    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.LOGIN_EXPIRED, "用户会话已过期，请重新登录");
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // 从 JWT 中获取用户信息
    public static User getUserFromToken(String token) {
        Claims claims;
        try {
            claims = validateToken(token);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.LOGIN_EXPIRED, "用户会话已过期，请重新登录");
        }
        User user = new User();
        user.setUser_id(claims.get("userId", Long.class));
        user.setUsername(claims.get("username", String.class));
        user.setName(claims.get("name", String.class));
        user.setRole(claims.get("role", String.class));
        user.setAvatar(claims.get("avatar", String.class));
        user.setMobile(claims.get("mobile", String.class));
        user.setStatus(claims.get("status", Integer.class));
        // 处理 createTime
        String createTimeStr = claims.get("createTime", String.class);
        if (createTimeStr != null) {
            user.setCreateTime(LocalDateTime.parse(createTimeStr, formatter));
        }

        // 处理 updateTime
        String updateTimeStr = claims.get("updateTime", String.class);
        if (updateTimeStr != null) {
            user.setUpdateTime(LocalDateTime.parse(updateTimeStr, formatter));
        }
        return user;
    }
}
