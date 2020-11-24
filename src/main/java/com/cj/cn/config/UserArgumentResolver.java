package com.cj.cn.config;

import com.cj.cn.pojo.User;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //表示SpringMVC请求方法中的参数为User类的时候调用resolveArgument方法
        Class<?> clazz = parameter.getParameterType();
//        return clazz == User.class;
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        String userKey = "";
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(ConstUtil.cookieName)) {
                userKey = cookie.getValue();
                break;
            }
        }
        log.info("执行了resolveArgument方法...");
        String userJsonStr = stringRedisTemplate.opsForValue().get(userKey);
        return JsonUtil.strToObj(userJsonStr, User.class);
    }
}
