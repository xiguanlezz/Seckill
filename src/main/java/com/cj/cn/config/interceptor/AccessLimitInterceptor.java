package com.cj.cn.config.interceptor;

import com.cj.cn.annotation.AccessLimit;
import com.cj.cn.cache.UserContext;
import com.cj.cn.pojo.User;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            log.info("执行了AccessLimitInterceptor的拦截方法...");
            User user = UserContext.getUser();
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                //没有写AccessLimit的注解直接放行即可
                return true;
            }

            //限制x秒内最多访问y次
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            if (needLogin) {
                if (user == null) {
                    render(response, CodeEnum.NO_LOGIN);
                    return false;
                }
            }
            String key = request.getRequestURI() + "_" + user.getId();
            String countStr = redisUtil.get(key);
            long count = 0;
            if (StringUtils.isEmpty(countStr)) {
                count = 1;
                redisUtil.set(key, count + "", seconds, TimeUnit.SECONDS);
            } else {
                count = Integer.parseInt(countStr);
                if (count < maxCount) {
                    redisUtil.incr(key);
                } else {
                    render(response, CodeEnum.ACCESS_LIMIT_REACHED);
                    return false;
                }
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeEnum codeEnum) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream os = response.getOutputStream();
        String str = JsonUtil.objToStr(ResultResponse.error(codeEnum));
        os.write(str.getBytes("UTF-8"));
        os.flush();
        os.close();
    }
}
