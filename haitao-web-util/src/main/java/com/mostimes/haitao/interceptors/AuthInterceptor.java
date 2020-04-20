package com.mostimes.haitao.interceptors;

import com.alibaba.fastjson.JSON;
import com.mostimes.haitao.annotaions.LoginRequired;
import com.mostimes.haitao.util.CookieUtil;
import com.mostimes.haitao.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    CookieUtil cookieUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断被拦截的请求的访问的方法的注解（是否需要拦截）
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (loginRequired == null) {//不需要登录
            return true;
        }else { //需要登录
            String token = request.getParameter("token");

            if (StringUtils.isNotBlank(token)) {
                //通过jwt校验真假
                Map<String, Object> decode = JwtUtil.decode(token, "2020haitao", "127.0.0.1");

                if (decode == null) {
                    OutputStream out = response.getOutputStream();
                    String errorStr = JSON.toJSONString("tokenError");
                    out.write(errorStr.getBytes("UTF-8"));
                    out.flush();
                    out.close();
                    return false;
                }
                //将用户token的携带的用户信息写入
                request.setAttribute("id", decode.get("id"));
                request.setAttribute("nickname", decode.get("nickname"));
                //验证通过，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "token", token, 60 * 60 * 1, true);
                    CookieUtil.setCookie(request, response, "nickname", (String) decode.get("nickname"), 60 * 60 * 1, true);
                }
                return true;
            }
            OutputStream out = response.getOutputStream();
            String errorStr = JSON.toJSONString("tokenError");
            out.write(errorStr.getBytes("UTF-8"));
            out.flush();
            out.close();
            return false;
        }
    }
}