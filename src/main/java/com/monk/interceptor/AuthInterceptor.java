package com.monk.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.monk.config.Constant.IS_PRODUCTION;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    )
            throws Exception
    {

        final String originHeader = request.getHeader("Origin");

        if (!IS_PRODUCTION) {
            if(originHeader != null) {
                if(originHeader.equals("http://localhost:5173")) {
                    return true;
                }
            }
        }

        if (IS_PRODUCTION) {
            if(originHeader != null) {
                if(originHeader.equals("https://monk-digital.com")) {
                    return true;
                }
            }
        }
        // response.sendError(404);
        return true;
    }
}
