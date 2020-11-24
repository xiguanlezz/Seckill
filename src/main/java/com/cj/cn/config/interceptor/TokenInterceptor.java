package com.cj.cn.config.interceptor;

import com.cj.cn.exception.GlobalException;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行了TokenInterceptor的拦截方法...");
        if (!CookieUtil.isExistToken(request)) {
            throw new GlobalException(CodeEnum.COOKIE_EXPIRE);
        }
        return true;
    }
}
