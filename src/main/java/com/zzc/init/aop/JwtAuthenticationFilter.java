package com.zzc.init.aop;

import com.zzc.init.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * JwtAuthenticationFilter 负责身份认证：
 * <p>
 * 只负责检查请求是否包含有效的 JWT Token，并将解析的用户信息存入请求属性。
 * 其职责是验证用户是否已登录，不能决定是否具有执行某一特定操作的权限。
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 白名单：不需要认证的接口路径
    private static final List<String> WHITE_LIST = List.of("/user/login", "/api/register");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 如果请求路径在白名单中，直接放行
        if (isWhiteListed(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头中获取 Token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                // 验证并解析 JWT
                Claims claims = JwtUtil.validateToken(token);
                request.setAttribute("claims", claims);
                // 可以在这里将用户信息存入 SecurityContext 或请求属性，供后续使用
                // SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("claims", claims);
            } catch (Exception e) {
                // Token 无效或过期
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token无效或过期");
                return;
            }
        } else {
            // 请求未携带 Token 或 Token 格式错误
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token不存在或错误");
            return;
        }

        // 放行请求
        filterChain.doFilter(request, response);
    }

    // 判断路径是否在白名单中
    private boolean isWhiteListed(String path) {
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }
}
