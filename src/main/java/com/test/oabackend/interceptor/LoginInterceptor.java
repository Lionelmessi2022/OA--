package com.test.oabackend.interceptor;

import com.test.oabackend.domain.LoginUser;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;



// 登录拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        HttpSession session = request.getSession(false);
        Object obj = session == null ? null : session.getAttribute("LoginUser");

        if (obj == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录或登录已过期\",\"data\":null}");
            return false;
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/emp") || uri.startsWith("/dept") || uri.startsWith("/job")) {
            LoginUser loginUser = (LoginUser) obj;
            // 查询类（GET）请求所有登录用户都可访问，用于首页统计等展示
            // 新增/修改/删除（非 GET）请求仍仅限管理员
            boolean isRead = "GET".equalsIgnoreCase(request.getMethod());
            if (!isRead && !"admin".equals(loginUser.getRole())) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"msg\":\"无权限访问该资源\",\"data\":null}");
                return false;
            }
        }

        return true;
    }
}