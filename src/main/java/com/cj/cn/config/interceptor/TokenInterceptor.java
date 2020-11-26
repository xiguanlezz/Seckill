package com.cj.cn.config.interceptor;

import com.cj.cn.cache.UserContext;
import com.cj.cn.exception.GlobalException;
import com.cj.cn.pojo.User;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.CookieUtil;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行了TokenInterceptor的拦截方法...");
        String token = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(token)) {
            throw new GlobalException(CodeEnum.COOKIE_EXPIRE);
        }

        String userJsonStr = redisUtil.get(token);
        User user = JsonUtil.strToObj(userJsonStr, User.class);
        if (user == null) return false;
        if (UserContext.getUser() == null) {
            UserContext.setUser(user);
        }
        return true;
    }
}
